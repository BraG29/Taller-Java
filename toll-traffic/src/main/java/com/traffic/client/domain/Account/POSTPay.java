package com.traffic.client.domain.Account;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class POSTPay extends Account{

    private CreditCard creditCard;

    public POSTPay(){
    }

    public POSTPay(Integer accountNumber, LocalDate creationDate, CreditCard creditCard) {
        super(accountNumber, creationDate);
        this.creditCard = creditCard;
    }
}
