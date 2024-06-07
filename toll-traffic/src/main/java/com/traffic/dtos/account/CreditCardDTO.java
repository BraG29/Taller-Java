package com.traffic.dtos.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardDTO {

    private Long id;
    private String cardNumber;
    private String name;
    private String expireDate;

}
