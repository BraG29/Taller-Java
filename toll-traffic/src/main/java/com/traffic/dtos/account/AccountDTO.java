package com.traffic.dtos.account;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AccountDTO {

    protected Integer accountNumber;
    protected LocalDate creationDate;

    public AccountDTO() {
    }

    public AccountDTO(Integer accountNumber, LocalDate creationDate) {
        this.accountNumber = accountNumber;
        this.creationDate = creationDate;
    }
}
