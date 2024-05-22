package com.traffic.client.domain.Vehicle;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ForeignVehicle extends Vehicle {

    public ForeignVehicle() {
    }

    public ForeignVehicle(Long id, Tag tag, List<TollPass> tollPass) {
        super(id, tag, tollPass);
    }


    @Override
    public String toString(){
        return super.toString() + " ]";
    }
}
