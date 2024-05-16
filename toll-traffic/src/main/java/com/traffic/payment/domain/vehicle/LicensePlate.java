package com.traffic.payment.domain.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LicensePlate extends Identifier{

    private Long id;
    private String licensePlateNumber;
}
