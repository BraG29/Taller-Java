package com.traffic.client.application.impl;

import com.traffic.client.application.AccountService;
import com.traffic.client.domain.Account.Account;
import com.traffic.client.domain.Account.CreditCard;
import com.traffic.client.domain.Account.POSTPay;
import com.traffic.client.domain.Account.PREPay;
import com.traffic.client.domain.User.ForeignUser;
import com.traffic.client.domain.User.NationalUser;
import com.traffic.client.domain.User.User;
import com.traffic.client.domain.Vehicle.*;
import com.traffic.client.domain.repository.ClientModuleRepository;
import com.traffic.communication.Interface.CommunicationController;
import com.traffic.dtos.PaymentTypeData;
import com.traffic.dtos.account.CreditCardDTO;
import com.traffic.dtos.account.PostPayDTO;
import com.traffic.dtos.account.PrePayDTO;
import com.traffic.dtos.user.ForeignUserDTO;
import com.traffic.dtos.user.NationalUserDTO;
import com.traffic.dtos.user.TollCustomerDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.dtos.vehicle.*;
import com.traffic.exceptions.ExternalApiException;
import com.traffic.exceptions.NoAccountException;
import com.traffic.exceptions.NoCustomerException;
import com.traffic.payment.Interface.PaymentController;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class AccountServiceImpl implements AccountService {

    @Inject
    private ClientModuleRepository repo;

    @Inject
    private PaymentController paymentController;

    @Inject
    private CommunicationController communicationController;



    @Override
    public void prePay(Tag tag, Double cost) throws Exception {
        try{
            repo.prePay(tag.getId(), cost);

            //TODO evento prepago.
        }catch(Exception e){
            System.err.println(e.getMessage());
        }

    }

    @Override
    public void postPay(Tag tag, Double cost) throws Exception {

        if(tag == null){
            throw new NoCustomerException("El tag es vacio.");
        }

        try{
            repo.postPay(tag.getId(), cost);

        }catch (ExternalApiException e){
            throw e;

        }catch (Exception e){
            System.err.println(e.getMessage());
        }

    }

    @Override
    public Optional<List<Account>> getAccountByTag(Tag tag) {

        try{
            return repo.getAccountsByTag(tag.getId());
        }catch (Exception e){
            System.err.println(e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public void loadBalance(Long id, Double balance) throws Exception {

        try{
            repo.loadBalance(id, balance);
        } catch(Exception e){
            System.err.println(e.getMessage());
        }

    }

    @Override
    public Optional<Double> showBalance(Long id) {

        Optional<User> usrOPT = repo.getUserById(id);

        if(usrOPT.isPresent()){
            try{
                User usr = usrOPT.get();
                if (usr.getTollCustomer() != null && usr.getTollCustomer().getPrePay() != null){
                    return Optional.of(usr.getTollCustomer().getPrePay().getBalance());
                }
            }catch (Exception e){
                System.err.println(e.getMessage());
            }

        }

        return Optional.empty();
    }

    @Override
    public void linkCreditCard(Long id, CreditCard creditCard) {

        try{
            repo.linkCreditCard(id, creditCard);
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
    }

    
}
