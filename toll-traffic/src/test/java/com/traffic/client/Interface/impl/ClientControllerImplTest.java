package com.traffic.client.Interface.impl;

import com.traffic.client.Interface.ClientController;
import com.traffic.client.application.AccountService;
import com.traffic.client.application.UserService;
import com.traffic.client.application.VehicleService;
import com.traffic.client.application.impl.AccountServiceImpl;
import com.traffic.client.application.impl.UserServiceImpl;
import com.traffic.client.application.impl.VehicleServiceImpl;
import jakarta.inject.Inject;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;



@EnableAutoWeld
@AddPackages(ClientControllerImpl.class)
@AddPackages(AccountServiceImpl.class)
@AddPackages(VehicleServiceImpl.class)
@AddPackages(UserServiceImpl.class)
@AddPackages(ClientController.class)
@AddPackages(AccountService.class)
@AddPackages(VehicleService.class)
@AddPackages(UserService.class)
class ClientControllerImplTest {

    @Inject
    ClientController controller;

    @Inject
    AccountService accountService;

    @Inject
    VehicleService vehicleService;

    @Inject
    UserService usrService;

    @Test
    void listUsers(){

        System.out.println("Lista  de usuarios del sistema.");
        controller.listUsers();

    }

    @Test
    void addTollCostumer() {
    }

    @Test
    void linkVehicle() {
    }

    @Test
    void unLinkVehicle() {
    }

    @Test
    void showLinkedVehicles() {
    }

    @Test
    void loadBalance() {
    }

    @Test
    void showBalance() {
    }

    @Test
    void linkCreditCard() {
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