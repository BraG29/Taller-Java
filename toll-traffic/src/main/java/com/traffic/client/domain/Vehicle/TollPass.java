package com.traffic.client.domain.Vehicle;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TollPass {

    private LocalDate passDate;
    private Double cost;
    private PaymentTypeEnum paymentType;

    public TollPass(){}

    public TollPass(LocalDate passDate, Double cost, PaymentTypeEnum payment) {
        this.passDate = passDate;
        this.cost = cost;
        paymentType = payment;
    }
}
