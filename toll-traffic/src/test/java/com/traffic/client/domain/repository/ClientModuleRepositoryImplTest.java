package com.traffic.client.domain.repository;

import com.traffic.client.domain.User.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@EnableAutoWeld
@AddPackages(ClientModuleRepositoryImpl.class)
class ClientModuleRepositoryImplTest {

    @Inject
    ClientModuleRepository repo;

    @Test
    void users() {
        repo.usersInit();

        Optional<List<User>> usersOPT = repo.listUsers();

        if(usersOPT.isPresent()){

            List<User> users = usersOPT.get();

            for (User user : users){
                System.out.println("Nombre: " + user.getName() + " Tag vehiculo: "+ user.getLinkedCars().get(0).getVehicle().getTag().getTagId()
                        + " Numero de cuenta PostPay: " + user.getTollCustomer().getPostPay().getAccountNumber());
            }
        }

    }

    @Test
    void findByTag() {
    }

    @Test
    void listUsers() {
    }

    @Test
    void createUser() {
    }

    @Test
    void getUserById() {
    }

    @Test
    void getVehicleByTag() {
    }
}