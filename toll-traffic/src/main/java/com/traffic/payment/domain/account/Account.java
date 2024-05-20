package com.traffic.payment.domain.account;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Account {

    protected Integer accountNumber;
    protected LocalDate creationDate;

    public Account() {
    }

    public Account(Integer accountNumber, LocalDate creationDate) {
        this.accountNumber = accountNumber;
        this.creationDate = creationDate;
    }
}
