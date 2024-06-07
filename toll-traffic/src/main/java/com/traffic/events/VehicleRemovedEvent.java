package com.traffic.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleRemovedEvent extends CustomEvent{

    private Long userId;
    private Long vehicleId;

    public VehicleRemovedEvent(String description, Long userId, Long vehicleId){
        super(description);
        this.userId = userId;
        this.vehicleId = vehicleId;
    }

}
