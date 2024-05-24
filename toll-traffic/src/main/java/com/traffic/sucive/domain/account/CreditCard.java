package com.traffic.sucive.domain.account;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CreditCard {

    private String cardNumber;
    private String name;
    private LocalDate expireDate;

    public CreditCard() {
    }

    public CreditCard(String cardNumber, String name, LocalDate expireDate) {
        this.cardNumber = cardNumber;
        this.name = name;
        this.expireDate = expireDate;
    }
}






