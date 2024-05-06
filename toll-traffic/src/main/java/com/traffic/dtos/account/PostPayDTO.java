package com.traffic.dtos.account;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class PostPayDTO extends AccountDTO{

    private CreditCardDTO creditCardDTO;

    public PostPayDTO() {
    }

    public PostPayDTO(Integer accountNumber, LocalDate creationDate, CreditCardDTO creditCardDTO) {
        super(accountNumber, creationDate);
        this.creditCardDTO = creditCardDTO;
    }
}
