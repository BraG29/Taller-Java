package com.traffic.payment.domain.repository;

import com.traffic.dtos.account.CreditCardDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.payment.domain.user.User;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;

@ApplicationScoped
public class PaymentRepositoryImplementation implements PaymentRepository {
    //asumamos que nos estámos conectando a una BDD de verdad
    ArrayList<User> users;

    //creemos unos datos que simulen entradas ya existentes en la BDD
    @PostConstruct
    public void initialize(){
        users = new ArrayList<>();
        //users.add(new User());
    }

    public void addUser(User userToAdd){
        users.add(userToAdd);
        System.out.println(userToAdd.getName());
    }
}
