package com.traffic.client.domain.Account;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.Random;

@EqualsAndHashCode(callSuper = true)
@Data
public class PREPay extends Account{

    private Double balance;

    public PREPay(){
    }

    public PREPay(Long id, Integer accountNumber, LocalDate creationDate, Double balance) {
        super(id, accountNumber, creationDate);
        this.balance = balance;
    }

    public void pay(Double cost){
        this.balance -= cost;
    }

    public void loadBalance(Double balance){
        this.balance += balance;
    }

    public static Integer generateRandomAccountNumber(){
        Random random = new Random();

        int minNumber = 100000;
        int maxNumber = 999999;

        return random.nextInt(maxNumber - minNumber + 1) + minNumber;
    }

    @Override
    public String toString(){
        return super.toString() + " Tipo de cuenta: PrePaga"  + " Balance: " + balance + " ]";
    }
}
