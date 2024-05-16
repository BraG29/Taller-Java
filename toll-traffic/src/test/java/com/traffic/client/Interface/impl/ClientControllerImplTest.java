package com.traffic.client.Interface.impl;

import com.traffic.client.Interface.ClientController;
import com.traffic.client.application.AccountService;
import com.traffic.client.application.UserService;
import com.traffic.client.application.VehicleService;
import com.traffic.client.application.impl.AccountServiceImpl;
import com.traffic.client.application.impl.UserServiceImpl;
import com.traffic.client.application.impl.VehicleServiceImpl;
import com.traffic.client.domain.Account.CreditCard;
import com.traffic.client.domain.Account.POSTPay;
import com.traffic.client.domain.Account.PREPay;
import com.traffic.client.domain.User.NationalUser;
import com.traffic.client.domain.User.TollCustomer;
import com.traffic.client.domain.User.User;
import com.traffic.client.domain.Vehicle.*;
import com.traffic.client.domain.repository.ClientModuleRepository;
import com.traffic.client.domain.repository.ClientModuleRepositoryImpl;
import com.traffic.dtos.PaymentTypeData;
import com.traffic.dtos.account.CreditCardDTO;
import com.traffic.dtos.account.PostPayDTO;
import com.traffic.dtos.account.PrePayDTO;
import com.traffic.dtos.user.NationalUserDTO;
import com.traffic.dtos.user.TollCustomerDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.dtos.vehicle.*;
import com.traffic.exceptions.NoCustomerException;
import com.traffic.sucive.Interface.SuciveController;
import com.traffic.sucive.Interface.impl.SuciveControllerImpl;
import com.traffic.toll.Interface.impl.TollControllerImpl;
import com.traffic.toll.domain.repositories.TariffRepository;
import com.traffic.toll.domain.repositories.VehicleRepository;
import com.traffic.toll.domain.repositories.impl.TariffRepositoryImpl;
import com.traffic.toll.domain.repositories.impl.VehicleRepositoryImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.weld.junit.MockBean;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@ExtendWith(WeldJunit5Extension.class)
class ClientControllerImplTest {

    @WeldSetup
    public static WeldInitiator weld;

    @BeforeAll
    public static void setup(){

       weld = WeldInitiator.from(ClientControllerImpl.class)
                .addBeans(MockBean.builder()
                        .types(ClientModuleRepository.class)
                        .scope(ApplicationScoped.class)
                        .creating(new ClientModuleRepositoryImpl())
                        .build())
                .addBeans(MockBean.builder()
                        .types(AccountService.class)
                        .scope(ApplicationScoped.class)
                        .creating(new AccountServiceImpl())
                        .build())
                .addBeans(MockBean.builder()
                        .types(VehicleService.class)
                        .scope(ApplicationScoped.class)
                        .creating(new VehicleServiceImpl())
                        .build())
                .addBeans(MockBean.builder()
                        .types(UserService.class)
                        .scope(ApplicationScoped.class)
                        .creating(new UserServiceImpl())
                        .build())
                .build();
    }
/*
    @Inject
    ClientController controller;

    @Inject
    ClientModuleRepository repo;
*/

    @Test
    void listUsers(ClientController controller ){

        System.out.println("Lista  de usuarios del sistema.");
        System.out.println(controller.listUsers().get().get(1).getName());

    }

    @Test
    void addTollCostumer(ClientController controller) {



        System.out.println("Lista de usuarios antes de agregar:");

        List<User> user = controller.listUsers().get();

        for(User usr: user){
            System.out.println(usr.getId() + usr.getName() + usr.getCi() + usr.getEmail());
        }

        System.out.println("Lista de usuarios despues de agregar:");

        //controller.addTollCostumer(testUser);

        /*
        for(User usr: controller.listUsers().get()){
            System.out.println(usr.getId() + usr.getName() + usr.getCi() + usr.getEmail());
        }*/

    }

    @Test
    void linkVehicle(ClientController controller) {

    }

    @Test
    void unLinkVehicle(ClientController controller) {
    }

    @Test
    void showLinkedVehicles(ClientController controller) {
    }

    @Test
    void loadBalanceANDShowBalance(ClientController controller, ClientModuleRepository repo) throws NoCustomerException {

        controller.loadBalance(51L, 2000D);
        System.out.println("Cargo balance al usuario con id 51");

        System.out.println("Balance1 " + controller.showBalance(51L).get());

    }

    @Test
    void linkCreditCard(ClientController controller) {
    }

    @Test
    void showPastPassages() {
    }

    @Test
    void showPastPassagesVehicle() {
    }

    @Test
    void getAccountByTag() {
    }

    @Test
    void prePay() {
    }

    @Test
    void postPay() {
    }


}