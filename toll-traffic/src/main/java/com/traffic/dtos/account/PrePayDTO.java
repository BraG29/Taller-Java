package com.traffic.dtos.account;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PrePayDTO extends AccountDTO{

    private Double balance;

    public PrePayDTO(Integer accountNumber, LocalDate creationDate, Double balance) {
        super(accountNumber, creationDate);
        this.balance = balance;
    }
}
