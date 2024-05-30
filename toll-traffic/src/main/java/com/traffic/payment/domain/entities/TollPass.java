package com.traffic.payment.domain.entities;

import com.traffic.dtos.PaymentTypeData;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor

@Entity(name = "Payment_TollPass")
public class TollPass {

    //    private Long id;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate passDate;
    private Double cost;
    private PaymentTypeData paymentType;
    @ManyToOne
    private Vehicle vehicle;


    public TollPass(Long id, LocalDate passDate, Double cost, PaymentTypeData paymentType){
        this.id = id;
        this.passDate = passDate;
        this.cost = cost;
        this.paymentType = paymentType;
    }

    @Override
    public String toString(){
        return "[Id: " + id + " Fecha: " + passDate+ " Costo: " + cost + " Tipo de pago: " + paymentType + "]";
    }
}