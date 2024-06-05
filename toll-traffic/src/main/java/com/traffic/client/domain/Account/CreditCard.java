package com.traffic.client.domain.Account;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity(name = "ClientModule_CreditCard")
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cardNumber;
    private String name;
    private LocalDate expireDate;

    public CreditCard(){}

    public CreditCard(Long id, String cardNumber, String name, LocalDate expireDate) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.name = name;
        this.expireDate = expireDate;
    }

    @Override
    public String toString(){
        return "Tarjeta de crédito [ Id: " + id +
                " Nombre: " + name + " Nº tarjeta: " + cardNumber + " Fecha de vencimiento: " + expireDate.toString() + " ]";
    }
}
