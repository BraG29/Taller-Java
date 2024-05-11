package com.traffic.dtos.vehicle;

import com.traffic.dtos.PaymentTypeData;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class TollPassDTO {

    private LocalDate date;
    private Double cost;
    private PaymentTypeData paymentType;

}
