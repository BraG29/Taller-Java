package com.traffic.toll.Interface.impl;

import com.traffic.client.Interface.ClientController;
import com.traffic.dtos.PaymentTypeData;
import com.traffic.dtos.account.AccountDTO;
import com.traffic.dtos.account.PostPayDTO;
import com.traffic.dtos.account.PrePayDTO;
import com.traffic.dtos.vehicle.IdentifierDTO;
import com.traffic.dtos.vehicle.LicensePlateDTO;
import com.traffic.dtos.vehicle.TagDTO;
import com.traffic.exceptions.ExternalApiException;
import com.traffic.exceptions.InternalErrorException;
import com.traffic.exceptions.InvalidVehicleException;
import com.traffic.exceptions.NoCustomerException;
import com.traffic.sucive.Interface.SuciveController;
import com.traffic.toll.Interface.TollController;
import com.traffic.toll.domain.entities.*;
import com.traffic.toll.domain.repositories.TariffRepository;
import com.traffic.toll.domain.repositories.VehicleRepository;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.annotations.Pos;

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

    public TollControllerImpl() {
    }

    private void addTollPass(Vehicle vehicle, Double cost, PaymentTypeData paymentType){
        TollPass tollPass = new TollPass(LocalDate.now(), cost, paymentType);
        vehicle.getTollPasses().add(tollPass);
        vehicleRepository.update(vehicle);
    }

    @Override
    public Optional<Boolean> isEnabled(IdentifierDTO identifier) throws IllegalArgumentException, InvalidVehicleException {

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
