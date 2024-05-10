package com.traffic.client.application.impl;

import com.traffic.client.application.AccountService;
import com.traffic.client.domain.Account.Account;
import com.traffic.client.domain.User.User;
import com.traffic.client.domain.Vehicle.Tag;
import com.traffic.client.domain.repository.ClientModuleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AccountServiceImpl implements AccountService {

    @Inject
    private ClientModuleRepository repo;

    @Override
    public void prePay(Tag tag, Double cost) {

    }

    @Override
    public void postPay(Tag tag, Double cost) {

    }

    @Override
    public Optional<List<Account>> getAccountByTag(Tag tag) {

        User usr = repo.findByTag(tag); //Este usuario lo voy a traer desde un repositorio que  hablara con una BD en el futuro. Injectando uno

        if(usr.getTollCustomer() != null){//si es cliente

            //el usuario siempre tiene prepaga, SI ES CLIENTE.
            List<Account> accounts = new ArrayList<Account>();
            accounts.add(usr.getTollCustomer().getPrePay());

            if(usr.getTollCustomer().getPostPay() != null){ //agrego postpaga, si tiene.

                accounts.add(usr.getTollCustomer().getPostPay());
            }
            return Optional.of(accounts);//devuelvo cuentas

        }//Si el usuario no es cliente devuelvo vacio

        return Optional.empty();

    }
}
