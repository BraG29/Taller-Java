package com.traffic.client.application;

import com.traffic.client.domain.Account.Account;
import com.traffic.client.domain.Account.CreditCard;
import com.traffic.client.domain.Vehicle.Tag;
import com.traffic.exceptions.ExternalApiException;
import com.traffic.exceptions.InvalidVehicleException;
import com.traffic.exceptions.NoAccountException;
import com.traffic.exceptions.NoCustomerException;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    public Boolean prePay(Tag tag, Double cost) throws NoAccountException,  NoCustomerException;

    public Boolean postPay(Tag tag, Double cost) throws NoAccountException, NoCustomerException, ExternalApiException, InvalidVehicleException;

    public Optional<List<Account>> getAccountByTag(Tag tag);

    public void loadBalance(Long id, Double balance) throws NoCustomerException;

    public Optional<Double> showBalance(Long id);

    public void linkCreditCard(Long id, CreditCard creditCard) ;
}
