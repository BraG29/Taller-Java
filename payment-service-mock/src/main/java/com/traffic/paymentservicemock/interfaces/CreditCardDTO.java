package com.traffic.paymentservicemock.interfaces;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardDTO {

    /*
    Formato del JSON
    {
        "name" : "Eduardo",
        "cardNumber" : "5235-1241-1253-6534"
    }
     */

    private String name;
    private String cardNumber;
    //TODO: Agregar fecha cuando podamos con los JSON

}
