package com.traffic.client.domain.Vehicle;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class NationalVehicle extends Vehicle {

    private LicensePlate plate;

    public NationalVehicle() {}

    public NationalVehicle(Tag tag, List<TollPass> tollPass, LicensePlate plate) {
        super(tag, tollPass);
        this.plate = plate;
    }

}
