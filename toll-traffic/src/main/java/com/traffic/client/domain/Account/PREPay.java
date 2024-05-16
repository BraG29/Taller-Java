package com.traffic.client.domain.Account;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class PREPay extends Account{

    private Double balance;

    public PREPay(){
    }

    public PREPay(Long id, Integer accountNumber, LocalDate creationDate, Double balance) {
        super(id, accountNumber, creationDate);
        this.balance = balance;
    }

    public void pay(Double cost){
        this.balance -= cost;
    }

    public void loadBalance(Double balance){
        this.balance += balance;
    }
}
