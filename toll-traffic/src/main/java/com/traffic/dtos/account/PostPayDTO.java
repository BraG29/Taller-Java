package com.traffic.dtos.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PostPayDTO extends AccountDTO{

    private CreditCardDTO creditCardDTO;

    public PostPayDTO(Integer accountNumber, LocalDate creationDate, CreditCardDTO creditCardDTO) {
        super(accountNumber, creationDate);
        this.creditCardDTO = creditCardDTO;
    }
}
