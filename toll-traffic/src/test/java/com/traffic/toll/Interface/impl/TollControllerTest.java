package com.traffic.toll.Interface.impl;

import com.traffic.client.Interface.ClientController;
import com.traffic.client.Interface.impl.ClientControllerImpl;
import com.traffic.dtos.account.AccountDTO;
import com.traffic.dtos.account.CreditCardDTO;
import com.traffic.dtos.account.PostPayDTO;
import com.traffic.dtos.account.PrePayDTO;
import com.traffic.dtos.vehicle.IdentifierDTO;
import com.traffic.dtos.vehicle.LicensePlateDTO;
import com.traffic.dtos.vehicle.TagDTO;
import com.traffic.exceptions.InvalidVehicleException;
import com.traffic.exceptions.NoAccountException;
import com.traffic.exceptions.NoCustomerException;
import com.traffic.sucive.Interface.SuciveController;
import com.traffic.sucive.Interface.impl.SuciveControllerImpl;
import com.traffic.toll.domain.entities.CommonTariff;
import com.traffic.toll.domain.entities.PreferentialTariff;
import com.traffic.toll.domain.repositories.TariffRepository;
import com.traffic.toll.domain.repositories.VehicleRepository;
import com.traffic.toll.domain.repositories.impl.TariffRepositoryImpl;
import com.traffic.toll.domain.repositories.impl.VehicleRepositoryImpl;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.weld.junit.MockBean;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(WeldJunit5Extension.class)
class TollControllerTest {

    @WeldSetup
    public WeldInitiator weld = WeldInitiator.from(TollControllerImpl.class)
            .addBeans(MockBean.builder()
                    .types(SuciveController.class)
                    .scope(ApplicationScoped.class)
                    .creating(new SuciveControllerImpl())
                    .build())
            .addBeans(MockBean.builder()
                    .types(VehicleRepository.class)
                    .scope(ApplicationScoped.class)
                    .creating(new VehicleRepositoryImpl())
                    .build())
            .addBeans(MockBean.builder()
                    .types(TariffRepository.class)
                    .scope(ApplicationScoped.class)
                    .creating(new TariffRepositoryImpl())
                    .build())
            .addBeans(MockBean.builder()
                    .types(ClientController.class)
                    .scope(ApplicationScoped.class)
                    .creating(createClientControllerMock())
                    .build())
            .build();


    private ClientController createClientControllerMock() {
        return new ClientControllerImpl() {
            @Override
            public Optional<List<AccountDTO>> getAccountByTag(TagDTO tag) throws IllegalArgumentException {

                if (tag.getUniqueId().equals(100L)) {
                    return Optional.of(
                            List.of(
                                    new PrePayDTO(
                                            1L,
                                            1234567890,
                                            LocalDate.of(2024, 2, 12),
                                            500D)));

                } else if( tag.getUniqueId().equals(101L) ){
                    return Optional.of(
                            List.of(
                                    new PostPayDTO(
                                            2L,
                                            98765432,
                                            LocalDate.of(2024, 1, 5),
                                            new CreditCardDTO())));

                } else if( tag.getUniqueId().equals(103L) ){
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
            public void prePay(Double balance, TagDTO tagDTO) throws NoAccountException, IllegalArgumentException, NoCustomerException {
            }

            @Override
            public void postPay(Double balance, TagDTO tagDTO) throws IllegalArgumentException, NoCustomerException {
            }
        };
    }

    @DisplayName("Tests para cuando isEnable devuelve true")
    @Test
    void isEnableSuccessTrueTest(TollControllerImpl tollController, VehicleRepository vehicleRepository) throws InvalidVehicleException {
        IdentifierDTO withPrePay = new TagDTO(100L);
        IdentifierDTO withPrePayLicensePlate = new LicensePlateDTO(1L, "ABC-123");
        IdentifierDTO withPostPay = new TagDTO(101L);
        IdentifierDTO withSucive = new TagDTO(102L);

        //Para catchear PersistenceErrorException
        Assertions.assertDoesNotThrow( () -> {
            //Caso de cliente que tiene cuenta PrePaga con saldo suficiente
            Assertions.assertTrue(tollController.isEnabled(withPrePay).orElse(false));
            //Caso de cliente que tiene cuenta PrePaga con saldo suficiente (buscado por LicensePlate)
            Assertions.assertTrue(tollController.isEnabled(withPrePayLicensePlate).orElse(false));
            //Caso de cliente que tiene cuenta PostPaga
            Assertions.assertTrue(tollController.isEnabled(withPostPay).orElse(false));
            //Caso de cliente nacional sin cuentas, se le cobra por Sucive
            Assertions.assertTrue(tollController.isEnabled(withSucive).orElse(false));
        });


    }

    @DisplayName("Tests para cuando isEnable devuelve false")
    @Test
    void isEnableSuccessFalseTest(TollControllerImpl tollController) throws InvalidVehicleException {
        IdentifierDTO withNotEnoughBalance = new TagDTO(103L);
        IdentifierDTO withNoAccounts = new TagDTO(105L);

        //Para catchear PersistenceErrorException
        Assertions.assertDoesNotThrow( () -> {
            //Caso de cliente extranjero con solo cuenta PrePaga sin saldo suficiente
            Assertions.assertFalse(tollController.isEnabled(withNotEnoughBalance).orElse(true));
            //Caso de cliente extranjero sin cuentas
            Assertions.assertFalse(tollController.isEnabled(withNoAccounts).orElse(true));
        });
    }

    @DisplayName("Tests para cuando isEnable lanza una excepcion")
    @Test
    void isEnableErrorTest(TollControllerImpl tollController){
        IdentifierDTO noExistentTag = new TagDTO(110L);
        IdentifierDTO noExistentLicensePlate = new LicensePlateDTO(15L, "RGC-429");

        //Caso de identifier en null
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
           tollController.isEnabled(null);
        });
        //Caso de que el vehiculo no exista (por Tag)
        Assertions.assertThrows(InvalidVehicleException.class, () -> {
            tollController.isEnabled(noExistentTag);
        });
        //Caso de que el vehiculo no exista (por LicensePlate)
        Assertions.assertThrows(InvalidVehicleException.class, () -> {
            tollController.isEnabled(noExistentLicensePlate);
        });
    }

    @DisplayName("Tests para actualizacion de tarifas correctas")
    @Test
    void updateTariffSuccessTest(TollControllerImpl tollController, TariffRepository tariffRepository) {
        Double amount = 150D;

        tollController.updatePreferentialTariff(amount);
        tollController.updateCommonTariff(amount);

        Assertions.assertEquals(tariffRepository.findPreferentialTariff().orElse(new PreferentialTariff()).getAmount(),
                amount);

        Assertions.assertEquals(tariffRepository.findCommonTariff().orElse(new CommonTariff()).getAmount(),
                amount);
    }

    @DisplayName("Test para cuando los updateTariff lanzan error")
    @Test
    void updateTariffErrorTest(TollControllerImpl tollController) {
        Double amount = -150D;

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> tollController.updateCommonTariff(amount));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> tollController.updatePreferentialTariff(amount));
    }
}