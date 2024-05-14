package com.traffic.payment.domain.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class Link {

    private Long id;
    private LocalDate initialDate;
    private Boolean active;
    private Vehicle vehicle;

}