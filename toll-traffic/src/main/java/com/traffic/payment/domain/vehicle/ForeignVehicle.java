package com.traffic.payment.domain.vehicle;

import com.traffic.dtos.vehicle.TagDTO;
import com.traffic.dtos.vehicle.TollPassDTO;
import com.traffic.dtos.vehicle.VehicleDTO;
import lombok.Getter;

import java.util.List;

@Getter
public class ForeignVehicle extends Vehicle {


    public ForeignVehicle(Long id, List<TollPass> tollPass, Tag tag) {
        super(id, tollPass, tag);
    }
}
