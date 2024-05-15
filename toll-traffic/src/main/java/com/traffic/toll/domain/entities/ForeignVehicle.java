package com.traffic.toll.domain.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ForeignVehicle extends Vehicle {

    public ForeignVehicle(Long id, Tag tag, List<TollPass> tollPasses) {
        super(id, tag, tollPasses);
    }

    public ForeignVehicle() {
    }
}
