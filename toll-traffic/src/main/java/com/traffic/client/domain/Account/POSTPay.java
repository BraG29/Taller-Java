package com.traffic.client.domain.Account;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@DiscriminatorValue("POSTPay")
public class POSTPay extends Account{

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "creditCard_id")
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
