package com.traffic.payment.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class POSTPay extends Account{

    @OneToOne
    @JoinColumn(name = "CreditCard_id")
    private CreditCard creditCard;

    public POSTPay(){
    }
    public POSTPay(Long id, Integer accountNumber, LocalDate creationDate, CreditCard creditCard) {
        super(id, accountNumber, creationDate);
        this.creditCard = creditCard;
    }


    public static Integer generateRandomAccountNumber(){
        return Account.generateRandomAccountNumber();
    }

    @Override
    public String toString(){
        return super.toString() + " Tipo de cuenta: PostPaga " + " Tarjeta: " + creditCard.toString() + " ]";
    }
}