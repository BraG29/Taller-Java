package com.traffic.payment.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "Payment_TollCustomer")
public class TollCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "POSTPay_id")
    private POSTPay postPay;

    @OneToOne
    @JoinColumn(name = "PREPay_id")
    private PREPay prePay;


    public TollCustomer(){}
    public TollCustomer(Long id, POSTPay postPay, PREPay prePay) {
        this.id = id;
        this.postPay = postPay;
        this.prePay = prePay;
    }
    @Override
    public String toString() {
        return "Cuenta/s del cliente [ Id:" + id + ", postPaga: " + postPay.toString() + ", prePaga: " + prePay.toString();
    }
}
