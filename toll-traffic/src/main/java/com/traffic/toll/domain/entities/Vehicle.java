package com.traffic.toll.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public abstract class Vehicle {

    protected Long id;
    protected Tag tag;
    protected List<TollPass> tollPasses;

    public Vehicle() {

    }

    public void updateEntity(Vehicle vehicle){
        this.setTag(vehicle.getTag());
        this.setTollPasses(vehicle.getTollPasses());
    }
}
