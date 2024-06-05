package com.traffic.toll.application.impl;

import com.traffic.client.Interface.local.ClientController;
import com.traffic.dtos.account.AccountDTO;
import com.traffic.dtos.account.PostPayDTO;
import com.traffic.dtos.account.PrePayDTO;
import com.traffic.dtos.vehicle.IdentifierDTO;
import com.traffic.dtos.vehicle.LicensePlateDTO;
import com.traffic.dtos.vehicle.TagDTO;
import com.traffic.events.CustomEvent;
import com.traffic.events.VehiclePassEvent;
import com.traffic.exceptions.*;
import com.traffic.sucive.Interface.SuciveController;
import com.traffic.toll.Interface.local.TollService;
import com.traffic.toll.domain.entities.*;
import com.traffic.toll.domain.repositories.IdentifierRepository;
import com.traffic.toll.domain.repositories.TariffRepository;
import com.traffic.toll.domain.repositories.VehicleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ApplicationScoped
public class TollServiceImpl implements TollService {

    @Inject
    private ClientController clientController;
    @Inject
    private SuciveController suciveController;
    @Inject
    private VehicleRepository vehicleRepository;
    @Inject
    private IdentifierRepository identifierRepository;
    @Inject
    private TariffRepository tariffRepository;
    @Inject
    private Event<CustomEvent> event;

    /**
     * Operacion para icializar vehiculos de pruebas
     * TODO: Eliminar para produccion
     */
    @Override
    @Transactional
    public void initVehicles(){
        List<Vehicle> vehicles = List.of(
                new NationalVehicle(null,
                        identifierRepository.findTagById(1L).orElseThrow(),
                        identifierRepository.findLicensePlateById(1L).orElseThrow()),
                new ForeignVehicle(null,
                        identifierRepository.findTagById(2L).orElseThrow())
//                ,
//                new NationalVehicle(null,
//                        identifierRepository.findTagById(3L).orElseThrow(),
//                        identifierRepository.findLicensePlateById(2L).orElseThrow()),
//                new ForeignVehicle(null,
//                        identifierRepository.findTagById(4L).orElseThrow()),
//                new NationalVehicle(null,
//                        identifierRepository.findTagById(5L).orElseThrow(),
//                        identifierRepository.findLicensePlateById(3L).orElseThrow()),
//                new ForeignVehicle(null,
//                        identifierRepository.findTagById(6L).orElseThrow())
        );

        for(Vehicle v : vehicles){
            vehicleRepository.save(v);
        }
    }

    private void fireTollPass(String message){
        event.fire(new VehiclePassEvent(message));
    }

    @Override
    public Optional<Boolean> isEnabled(IdentifierDTO identifier) throws IllegalArgumentException,
            InvalidVehicleException{

        if (identifier == null) {
            throw new IllegalArgumentException("identifier is null");
        }

        Optional<Vehicle> vehicleOPT = Optional.empty();

        if(identifier instanceof LicensePlateDTO){
            Optional<LicensePlate> licensePlateOPT = identifierRepository.findLicensePlateById(
                    identifier.getId());

            vehicleOPT = licensePlateOPT.flatMap(licensePlate ->
                    vehicleRepository.findByLicensePlate(licensePlate));
        } else{
            Optional<Tag> tagOPT = identifierRepository.findTagById(
                    identifier.getId());

            vehicleOPT = tagOPT.flatMap(tag ->
                vehicleRepository.findByTag(tag));
        }

        if (vehicleOPT.isPresent()) {
            Vehicle vehicle = vehicleOPT.get();
            TagDTO tagDTO = new TagDTO(vehicle.getTag().getId(),
                    vehicleOPT.get().getTag().getUniqueId().toString());

            try {

                final String TOLL_PASS_MESSAGE = "Se habilito la pasada para el vehiculo de tag: %s";
                try {
                    Optional<Tariff> tariffOPT = tariffRepository.findTariff(PreferentialTariff.class);
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
                            this.fireTollPass(TOLL_PASS_MESSAGE.formatted(vehicle.getTag().getUniqueId()));
                            return Optional.of(true);
                        }
                    }

                    if (accountDTOS.stream().anyMatch(a -> a instanceof PostPayDTO)) {
                        clientController.postPay(preferentialTariff.getAmount(), tagDTO);
                        this.fireTollPass(TOLL_PASS_MESSAGE.formatted(vehicle.getTag().getUniqueId()));
                        return Optional.of(true);
                    }

                } catch (NoSuchElementException e) {
                    System.out.println("El cliente no tiene cuentas");

                }

                if (vehicleOPT.get() instanceof NationalVehicle) {
                    System.out.println("Procediendo a cobro por Sucive");
                    Optional<Tariff> tariffOPT = tariffRepository.findTariff(CommonTariff.class);
                    CommonTariff commonTariff = (CommonTariff) tariffOPT.orElseThrow(() ->
                            new InternalErrorException("No hay tarifa comun")
                    );

                    LicensePlate licensePlate = ((NationalVehicle) vehicleOPT.get())
                            .getLicensePlate();

                    LicensePlateDTO licensePlateDTO = new LicensePlateDTO(licensePlate.getId(),
                            licensePlate.getLicensePlateNumber());

                    suciveController.notifyPayment(licensePlateDTO,
                            commonTariff.getAmount());

                    this.fireTollPass(TOLL_PASS_MESSAGE.formatted(vehicle.getTag().getUniqueId()));
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

        Tariff tariff = tariffRepository.findTariff(CommonTariff.class).orElseThrow();
        tariff.setAmount(amount);
        tariffRepository.save(tariff)
                .orElseThrow(() -> new PersistenceException(""));
    }

    @Override
    public void updatePreferentialTariff(Double amount) throws NoSuchElementException {

        if (amount < 0) {
            throw new IllegalArgumentException("El monto no es valido");
        }

        Tariff tariff = tariffRepository.findTariff(PreferentialTariff.class).orElseThrow();
        tariff.setAmount(amount);
        tariffRepository.save(tariff)
                .orElseThrow(() -> new PersistenceException(""));
    }
}
