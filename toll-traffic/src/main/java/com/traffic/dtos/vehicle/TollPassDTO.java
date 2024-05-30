package com.traffic.dtos.vehicle;

//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.fasterxml.jackson.annotation.JsonProperty;
import com.traffic.dtos.PaymentTypeData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TollPassDTO {

    private Long id;
    private LocalDate date;
    private Double cost;
    private PaymentTypeData paymentType;

    public TollPassDTO(){}

    public TollPassDTO(Long id,
                        LocalDate date,
                       Double cost,
                       PaymentTypeData paymentType) {
        this.id = id;
        this.date = date;
        this.cost = cost;
        this.paymentType = paymentType;
    }
}
