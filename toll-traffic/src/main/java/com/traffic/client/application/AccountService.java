package com.traffic.client.application;

import com.traffic.client.domain.Account.Account;
import com.traffic.client.domain.Vehicle.Tag;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    public void prePay(Tag tag, Double cost);

    public void postPay(Tag tag, Double cost);

    public Optional<List<Account>> getAccountByTag(Tag tag);


}
