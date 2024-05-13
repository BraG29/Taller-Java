package com.traffic.client.domain.Vehicle;

import com.traffic.dtos.PaymentTypeData;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TollPass {

    private Long id;
    private LocalDate passDate;
    private Double cost;
    private PaymentTypeData paymentType;

    public TollPass(){}

    public TollPass(Long id,LocalDate passDate, Double cost, PaymentTypeData payment) {
        this.passDate = passDate;
        this.cost = cost;
        paymentType = payment;
    }
}
