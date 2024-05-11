package com.traffic.dtos.account;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PrePayDTO extends AccountDTO{

    private Double balance;

    public PrePayDTO(Long id, Integer accountNumber, LocalDate creationDate, Double balance) {
        super(id, accountNumber, creationDate);
        this.balance = balance;
    }
}
