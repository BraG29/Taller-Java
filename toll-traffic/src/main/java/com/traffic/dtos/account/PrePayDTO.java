package com.traffic.dtos.account;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class PrePayDTO extends AccountDTO{

    private Double balance;

    public PrePayDTO() {
    }

    public PrePayDTO(Integer accountNumber, LocalDate creationDate, Double balance) {
        super(accountNumber, creationDate);
        this.balance = balance;
    }
}
