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
    private String date;
    private Double cost;
    private PaymentTypeData paymentType;
    private VehicleDTO vehicle;


    public TollPassDTO(){}

    public TollPassDTO(Long id,
                       String date,
                       Double cost,
                       PaymentTypeData paymentType) {
        this.id = id;
        this.date = date;
        this.cost = cost;
        this.paymentType = paymentType;
    }

    public TollPassDTO(Long id,
                       String date,
                       Double cost,
                       PaymentTypeData paymentType,
                       VehicleDTO vehicle) {
        this.id = id;
        this.date = date;
        this.cost = cost;
        this.paymentType = paymentType;
        this.vehicle = vehicle;

    }
}
