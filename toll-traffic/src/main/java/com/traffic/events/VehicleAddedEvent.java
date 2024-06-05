package com.traffic.events;

import com.traffic.dtos.vehicle.VehicleDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleAddedEvent extends CustomEvent{

    private Long userId;
    private VehicleDTO vehicle;

    public VehicleAddedEvent (String description, Long userId ,VehicleDTO vehicle){
        super(description);
        this.userId = userId;
        this.vehicle = vehicle;
    }
}
