package com.traffic.toll.domain.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class NationalVehicle extends Vehicle {

    private LicensePlate licensePlate;

    public NationalVehicle() {
        super();
    }

    public NationalVehicle(Long id, Tag tag, List<TollPass> tollPasses, LicensePlate licensePlate) {
        super(id, tag, tollPasses);
        this.licensePlate = licensePlate;
    }

    @Override
    public void updateEntity(Vehicle vehicle) {
        super.updateEntity(vehicle);
        NationalVehicle nationalVehicle = (NationalVehicle) vehicle;
        this.setLicensePlate(nationalVehicle.getLicensePlate());
    }
}
