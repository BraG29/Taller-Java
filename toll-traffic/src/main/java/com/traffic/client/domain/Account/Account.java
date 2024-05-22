package com.traffic.client.domain.Account;

import lombok.Data;

import java.time.LocalDate;

@Data
public abstract class Account {

    private Long id;
    private Integer accountNumber;
    private LocalDate creationDate;

    public Account(){
    }

    public Account(Long id, Integer accountNumber, LocalDate creationDate) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.creationDate = creationDate;
    }

    @Override
    public String toString(){
        return "Cuenta [Id: " + id +
                " Nº cuenta: " + accountNumber +
                " Fecha de creacion: " + creationDate.toString();
    }
}
