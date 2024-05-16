package com.traffic.payment.domain.vehicle;

import com.traffic.dtos.vehicle.LicensePlateDTO;
import com.traffic.dtos.vehicle.TagDTO;
import com.traffic.dtos.vehicle.TollPassDTO;
import com.traffic.dtos.vehicle.VehicleDTO;
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