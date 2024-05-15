package com.traffic.toll.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LicensePlate {

    private Long id;
    private String licensePlateNumber;

    public LicensePlate() {
    }
}
