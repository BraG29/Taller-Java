package com.traffic.dtos.account;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreditCardDTO {

    private String cardNumber;
    private String name;
    private LocalDate expireDate;

    public CreditCardDTO() {
    }

    public CreditCardDTO(String cardNumber, String name, LocalDate expireDate) {
        this.cardNumber = cardNumber;
        this.name = name;
        this.expireDate = expireDate;
    }
}
