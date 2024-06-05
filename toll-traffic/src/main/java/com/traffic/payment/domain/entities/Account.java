package com.traffic.payment.domain.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Random;

@Data
@Entity(name = "Payment_Account")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

public abstract class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public static Integer generateRandomAccountNumber(){
        Random random = new Random();

        int minNumber = 100000;
        int maxNumber = 999999;

        return random.nextInt(maxNumber - minNumber + 1) + minNumber;
    }

}
