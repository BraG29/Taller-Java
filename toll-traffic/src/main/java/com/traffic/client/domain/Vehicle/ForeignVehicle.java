package com.traffic.client.domain.Vehicle;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ForeignVehicle extends Vehicle {

    public ForeignVehicle() {
    }

    public ForeignVehicle(Tag tag, List<TollPass> tollPass) {
        super(tag, tollPass);
    }
}
