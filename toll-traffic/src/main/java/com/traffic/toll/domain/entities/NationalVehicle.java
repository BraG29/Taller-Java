package com.traffic.toll.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Entity(name = "Toll_National_Vehicle")
public class NationalVehicle extends Vehicle {

    @OneToOne
    @JoinColumn(name = "license_plate", unique = true)
    private LicensePlate licensePlate;

    public NationalVehicle() {
        super();
    }

    public NationalVehicle(Long id, Tag tag, LicensePlate licensePlate) {
        super(id, tag);
        this.licensePlate = licensePlate;
    }

//    @Override
//    public void updateEntity(Vehicle vehicle) {
//        super.updateEntity(vehicle);
//        NationalVehicle nationalVehicle = (NationalVehicle) vehicle;
//        this.setLicensePlate(nationalVehicle.getLicensePlate());
//    }
}
