package com.traffic.client.domain.Vehicle;

import com.traffic.dtos.PaymentTypeData;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TollPass {

    private LocalDate passDate;
    private Double cost;
    private PaymentTypeData paymentType;

}
