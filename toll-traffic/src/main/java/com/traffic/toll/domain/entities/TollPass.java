package com.traffic.toll.domain.entities;

import com.traffic.dtos.PaymentTypeData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class TollPass {


    private LocalDate date;
    private Double cost;
    private PaymentTypeData paymentType;

    public TollPass() {
    }
}
