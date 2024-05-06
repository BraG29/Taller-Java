package com.traffic.dtos.vehicle;

import com.traffic.dtos.PaymentTypeData;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TollPassDTO {

    private LocalDate date;
    private Double cost;
    private PaymentTypeData paymentType;

    public TollPassDTO() {
    }

    public TollPassDTO(LocalDate date, Double cost, PaymentTypeData paymentType) {
        this.date = date;
        this.cost = cost;
        this.paymentType = paymentType;
    }
}
