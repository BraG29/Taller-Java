package com.traffic.client.domain.Account;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.Random;

@EqualsAndHashCode(callSuper = true)
@Data
public class POSTPay extends Account{

    private CreditCard creditCard;

    public POSTPay(){
    }

    public POSTPay(Long id, Integer accountNumber, LocalDate creationDate, CreditCard creditCard) {
        super(id, accountNumber, creationDate);
        this.creditCard = creditCard;
    }


    public static Integer generateRandomAccountNumber(){
        Random random = new Random();

        int minNumber = 100000;
        int maxNumber = 999999;

        return random.nextInt(maxNumber - minNumber + 1) + minNumber;
    }

    @Override
    public String toString(){
        return super.toString() + " Tipo de cuenta: PostPaga " + " Tarjeta: " + creditCard.toString() + " ]";
    }

}
