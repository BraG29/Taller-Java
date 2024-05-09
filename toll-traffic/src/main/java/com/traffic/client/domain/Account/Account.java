package com.traffic.client.domain.Account;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Account {

    private Integer accountNumber;
    private LocalDate creationDate;

    public Account(){
    }

    public Account(Integer accountNumber, LocalDate creationDate) {
        this.accountNumber = accountNumber;
        this.creationDate = creationDate;
    }
}
