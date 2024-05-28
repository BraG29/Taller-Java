package com.traffic.sucive.domain.vehicle;

import lombok.Getter;

import java.util.List;

@Getter
public class NationalVehicle extends Vehicle {

    private LicensePlate licensePlate;

    public NationalVehicle(Long id, List<TollPass> tollPass, Tag tag, LicensePlate licensePlate) {
        super(id, tollPass, tag);
        this.licensePlate = licensePlate;
    }
}
