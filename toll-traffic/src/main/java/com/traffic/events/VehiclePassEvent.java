package com.traffic.events;

import com.traffic.dtos.vehicle.TollPassDTO;

public class VehiclePassEvent extends CustomEvent {

    Long vehicleId;
    TollPassDTO tollPass;


    public VehiclePassEvent(String description, Long vehicleId, TollPassDTO tollPass) {
        super(description);
        this.vehicleId = vehicleId;
        this.tollPass = tollPass;
    }

    public VehiclePassEvent(String description) {
        super(description);
    }
}
