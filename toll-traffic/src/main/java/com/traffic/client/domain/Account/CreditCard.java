package com.traffic.client.domain.Account;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreditCard {

    private Long id;
    private String cardNumber;
    private String name;
    private LocalDate expireDate;

    public CreditCard(){}

    public CreditCard(Long id, String cardNumber, String name, LocalDate expireDate) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.name = name;
        this.expireDate = expireDate;
    }
}
