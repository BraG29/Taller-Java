package com.traffic.client.domain.Vehicle;

import com.traffic.dtos.PaymentTypeData;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TollPass {

//    private Long id;
    private LocalDate passDate;
    private Double cost;
    private PaymentTypeData paymentType;


    @Override
    public String toString(){
        return "[ " + passDate + cost + paymentType + " ]";
    }
}
