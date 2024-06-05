package com.traffic.payment.domain.entities;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "Payment_PrePay")
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
        return Account.generateRandomAccountNumber();
    }

    @Override
    public String toString(){
        return super.toString() + " Tipo de cuenta: PrePaga"  + " Balance: " + balance + " ]";
    }
}