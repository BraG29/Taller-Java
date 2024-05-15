package com.traffic.toll.Interface.impl;

import com.traffic.client.Interface.ClientController;
import com.traffic.client.Interface.impl.ClientControllerImpl;
import com.traffic.dtos.account.AccountDTO;
import com.traffic.dtos.account.PrePayDTO;
import com.traffic.dtos.vehicle.IdentifierDTO;
import com.traffic.dtos.vehicle.TagDTO;
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


    private ClientController createClientControllerMock(){
        return new ClientControllerImpl(){
            @Override
            public Optional<List<AccountDTO>> getAccountByTag(TagDTO tag) throws IllegalArgumentException {
                return Optional.of(
                        List.of(
                                new PrePayDTO(
                                        1L,
                                        1234567890,
                                        LocalDate.of(2024,2,12),
                                        500D)));
            }
        };
    }

    @Test
    void isEnableTest(TollControllerImpl tollController){
        IdentifierDTO identifier = new TagDTO(100L);

        Assertions.assertTrue( tollController.isEnabled(identifier).orElse(false) );
    }

    @Test
    void updateTariffSuccessTest(TollControllerImpl tollController, TariffRepository tariffRepository){
        Double amount = 150D;

        tollController.updatePreferentialTariff(amount);
        tollController.updateCommonTariff(amount);

        Assertions.assertEquals(tariffRepository.findPreferentialTariff().orElse(new PreferentialTariff()).getAmount(),
                amount);

        Assertions.assertEquals(tariffRepository.findCommonTariff().orElse(new CommonTariff()).getAmount(),
                amount);
    }

    @Test
    void updateTariffErrorTest(TollControllerImpl tollController){
        Double amount = -150D;

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> tollController.updateCommonTariff(amount));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> tollController.updatePreferentialTariff(amount));
    }
}