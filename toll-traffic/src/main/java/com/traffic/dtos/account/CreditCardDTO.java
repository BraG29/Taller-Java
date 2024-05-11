package com.traffic.dtos.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CreditCardDTO {

    private Long id;
    private String cardNumber;
    private String name;
    private LocalDate expireDate;

}
