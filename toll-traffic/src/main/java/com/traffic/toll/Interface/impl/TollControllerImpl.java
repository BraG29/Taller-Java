package com.traffic.toll.Interface.impl;

import com.traffic.client.Interface.local.ClientController;
import com.traffic.dtos.PaymentTypeData;
import com.traffic.dtos.account.AccountDTO;
import com.traffic.dtos.account.PostPayDTO;
import com.traffic.dtos.account.PrePayDTO;
import com.traffic.dtos.vehicle.IdentifierDTO;
import com.traffic.dtos.vehicle.LicensePlateDTO;
import com.traffic.dtos.vehicle.TagDTO;
import com.traffic.exceptions.*;
import com.traffic.sucive.Interface.SuciveController;
import com.traffic.toll.Interface.TollController;
import com.traffic.toll.domain.entities.*;
import com.traffic.toll.domain.repositories.TariffRepository;
import com.traffic.toll.domain.repositories.VehicleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ApplicationScoped
public class TollControllerImpl implements TollController {

    @Inject
    private ClientController clientController;
    @Inject
    private SuciveController suciveController;
    @Inject
    private VehicleRepository vehicleRepository;
    @Inject
    private TariffRepository tariffRepository;
//    @Inject
//    private Event<CustomEvent> singleEvent;

    public TollControllerImpl() {
    }

    private void addTollPass(Vehicle vehicle, Double cost, PaymentTypeData paymentType) throws PersistenceErrorException {
        TollPass tollPass = new TollPass(LocalDate.now(), cost, paymentType);
        vehicle.getTollPasses().add(tollPass);

        Optional<Vehicle> vehicleOPT = vehicleRepository.update(vehicle);

        vehicleOPT.orElseThrow(() -> new PersistenceErrorException("No se ha podido actualizar el vehiculo"));

//        vehicleOPT.ifPresent( v -> {
//            CustomEvent event = new NewTollPassEvent(
//                    "Se le ha agregado una nueva pasada por peaje al vehiculo con tag: " + v.getTag().getUniqueId(),
//                    v.getId(),
//                    new TollPassDTO(tollPass.getDate(), tollPass.getCost(), tollPass.getPaymentType()));
//            singleEvent.fire(event);
//        });
    }

    @Override
    public Optional<Boolean> isEnabled(IdentifierDTO identifier) throws IllegalArgumentException, InvalidVehicleException, PersistenceErrorException {

        if (identifier == null) {
            throw new IllegalArgumentException("identifier is null");
        }

        Optional<Vehicle> vehicleOPT = (identifier instanceof LicensePlateDTO) ?
                vehicleRepository.findByLicensePlate((LicensePlateDTO) identifier) :
                vehicleRepository.findByTag((TagDTO) identifier);

        if (vehicleOPT.isPresent()) {
            TagDTO tagDTO = new TagDTO(vehicleOPT
                    .get()
                    .getTag()
                    .getUniqueId());

            try {

                try {
                    Optional<Tariff> tariffOPT = tariffRepository.findPreferentialTariff();
                    PreferentialTariff preferentialTariff = (PreferentialTariff) tariffOPT.orElseThrow(() ->
                            new InternalErrorException("No hay tarifa preferencial")
                    );

                    Optional<List<AccountDTO>> accountsOPT = clientController.getAccountByTag(tagDTO);

                    List<AccountDTO> accountDTOS = accountsOPT.orElseThrow();

                    if (accountDTOS.stream().anyMatch(a -> a instanceof PrePayDTO)) {

                        PrePayDTO prePayAccount = (PrePayDTO) accountDTOS.stream()
                                .filter(a -> a instanceof PrePayDTO)
                                .findFirst()
                                .get();

                        if (prePayAccount.getBalance() >= preferentialTariff.getAmount()) {
                            clientController.prePay(preferentialTariff.getAmount(), tagDTO);
                            addTollPass(vehicleOPT.get(),
                                    preferentialTariff.getAmount(),
                                    PaymentTypeData.PRE_PAYMENT);
                            return Optional.of(true);
                        }
                    }

                    if (accountDTOS.stream().anyMatch(a -> a instanceof PostPayDTO)) {
                        clientController.postPay(preferentialTariff.getAmount(), tagDTO);
                        addTollPass(vehicleOPT.get(),
                                preferentialTariff.getAmount(),
                                PaymentTypeData.POST_PAYMENT);
                        return Optional.of(true);
                    }

                } catch (NoSuchElementException e) {
                    System.out.println("El cliente no tiene cuentas");

                } catch (NoCustomerException e) {
                    System.err.println(e.getMessage());

                } catch (NoAccountException e) {
                    throw new RuntimeException(e);
                }

                if (vehicleOPT.get() instanceof NationalVehicle) {
                    System.out.println("Procediendo a cobro por Sucive");
                    Optional<Tariff> tariffOPT = tariffRepository.findCommonTariff();
                    CommonTariff commonTariff = (CommonTariff) tariffOPT.orElseThrow(() ->
                            new InternalErrorException("No hay tarifa comun")
                    );

                    LicensePlate licensePlate = ((NationalVehicle) vehicleOPT.get())
                            .getLicensePlate();

                    LicensePlateDTO licensePlateDTO = new LicensePlateDTO(licensePlate.getId(), licensePlate.getLicensePlateNumber());

                    suciveController.notifyPayment(licensePlateDTO,
                            commonTariff.getAmount());

                    addTollPass(vehicleOPT.get(),
                            commonTariff.getAmount(),
                            PaymentTypeData.SUCIVE);
                    return Optional.of(true);

                }

            } catch (InternalErrorException e) {
                //Fatal Error
                System.err.println(e.getMessage());

            } catch (ExternalApiException e) {
                System.err.println(e.getMessage());

            }
        } else{
          throw new InvalidVehicleException("No existe vehiculo con esa identificacion");
        }

        return Optional.of(false);
    }

    @Override
    public void updateCommonTariff(Double amount) {

        if( amount < 0 ){
            throw new IllegalArgumentException("El monto no es valido");
        }

        tariffRepository.updateCommonTariff(amount);

    }

    @Override
    public void updatePreferentialTariff(Double amount) {

        if( amount < 0 ){
            throw new IllegalArgumentException("El monto no es valido");
        }

        tariffRepository.updatePreferentialTariff(amount);
    }
}
