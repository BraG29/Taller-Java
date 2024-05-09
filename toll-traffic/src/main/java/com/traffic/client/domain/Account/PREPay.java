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

    public PREPay(Integer accountNumber, LocalDate creationDate, Double balance) {
        super(accountNumber, creationDate);
        this.balance = balance;
    }

}
