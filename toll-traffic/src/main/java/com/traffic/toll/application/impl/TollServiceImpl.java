package com.traffic.toll.application.impl;

import com.traffic.client.Interface.ClientController;
import com.traffic.client.Interface.impl.ClientControllerImpl;
import com.traffic.dtos.PaymentTypeData;
import com.traffic.dtos.account.AccountDTO;
import com.traffic.dtos.account.CreditCardDTO;
import com.traffic.dtos.account.PostPayDTO;
import com.traffic.dtos.account.PrePayDTO;
import com.traffic.dtos.vehicle.IdentifierDTO;
import com.traffic.dtos.vehicle.LicensePlateDTO;
import com.traffic.dtos.vehicle.TagDTO;
import com.traffic.exceptions.*;
import com.traffic.sucive.Interface.SuciveController;
import com.traffic.sucive.Interface.impl.SuciveControllerImpl;
import com.traffic.toll.Interface.local.TollService;
import com.traffic.toll.domain.entities.*;
import com.traffic.toll.domain.repositories.IdentifierRepository;
import com.traffic.toll.domain.repositories.TariffRepository;
import com.traffic.toll.domain.repositories.VehicleRepository;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import org.jboss.jdeparser.FormatPreferences;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ApplicationScoped
public class TollServiceImpl implements TollService {

//    @Inject
    private ClientController clientController;
//    @Inject
    private SuciveController suciveController;
    @Inject
    private VehicleRepository vehicleRepository;
    @Inject
    private IdentifierRepository identifierRepository;
    @Inject
    private TariffRepository tariffRepository;
//    @Inject
//    private Event<CustomEvent> singleEvent;

    @PostConstruct
    public void setUp(){
        //Creacion de Stubs que seran usado hasta que se haga la integracion con otros modulos
        clientController = new ClientControllerImpl(){
            @Override
            public Optional<List<AccountDTO>> getAccountByTag(TagDTO tag) throws IllegalArgumentException {

                if (tag.getId().equals(1L)) {
                    return Optional.of(
                            List.of(
                                    new PrePayDTO(
                                            1L,
                                            1234567890,
                                            LocalDate.of(2024, 2, 12),
                                            500D)));

                } else if( tag.getId().equals(101L) ){
                    return Optional.of(
                            List.of(
                                    new PostPayDTO(
                                            2L,
                                            98765432,
                                            LocalDate.of(2024, 1, 5),
                                            new CreditCardDTO())));

                } else if( tag.getId().equals(103L) ){
                    return Optional.of(
                            List.of(
                                    new PrePayDTO(
                                            1L,
                                            1234567890,
                                            LocalDate.of(2024, 2, 12),
                                            10D)));

                } else{
                    return Optional.empty();
                }
            }

            @Override
            public Boolean postPay(Double balance, TagDTO tagDTO) throws IllegalArgumentException, NoCustomerException {
                return true;
            }

            @Override
            public Boolean prePay(Double balance, TagDTO tagDTO) throws NoAccountException, IllegalArgumentException, NoCustomerException {
                return true;
            }
        };

        suciveController = new SuciveControllerImpl(){
            @Override
            public void notifyPayment(LicensePlateDTO licensePlate, Double amount) {
            }
        };
    }

    @Override
    public Optional<Boolean> isEnabled(IdentifierDTO identifier) throws IllegalArgumentException, InvalidVehicleException{

        if (identifier == null) {
            throw new IllegalArgumentException("identifier is null");
        }

        Optional<Vehicle> vehicleOPT = Optional.empty();

        if(identifier instanceof LicensePlateDTO){
            Optional<LicensePlate> licensePlateOPT = identifierRepository.findLicensePlateById(
                    ((LicensePlateDTO) identifier).getId());

            vehicleOPT = licensePlateOPT.flatMap(licensePlate ->
                    vehicleRepository.findByLicensePlate(licensePlate));
        } else{
            Optional<Tag> tagOPT = identifierRepository.findTagById(
                    ((TagDTO) identifier).getId());

            vehicleOPT = tagOPT.flatMap(tag ->
                vehicleRepository.findByTag(tag));
        }

        if (vehicleOPT.isPresent()) {
            TagDTO tagDTO = new TagDTO(vehicleOPT.get().getTag().getId(),
                    vehicleOPT.get().getTag().getUniqueId().toString());

            try {

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
                            return Optional.of(true);
                        }
                    }

                    if (accountDTOS.stream().anyMatch(a -> a instanceof PostPayDTO)) {
                        clientController.postPay(preferentialTariff.getAmount(), tagDTO);
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
                    Optional<Tariff> tariffOPT = tariffRepository.findTariff(CommonTariff.class);
                    CommonTariff commonTariff = (CommonTariff) tariffOPT.orElseThrow(() ->
                            new InternalErrorException("No hay tarifa comun")
                    );

                    LicensePlate licensePlate = ((NationalVehicle) vehicleOPT.get())
                            .getLicensePlate();

                    LicensePlateDTO licensePlateDTO = new LicensePlateDTO(licensePlate.getId(), licensePlate.getLicensePlateNumber());

                    suciveController.notifyPayment(licensePlateDTO,
                            commonTariff.getAmount());

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
