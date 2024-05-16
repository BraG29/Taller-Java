package com.traffic.events;

import com.traffic.dtos.vehicle.TollPassDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NewTollPassEvent extends CustomEvent {

    Long vehicleId;
    TollPassDTO tollPass;


    public NewTollPassEvent(String description, Long vehicleId, TollPassDTO tollPass) {
        super(description);
        this.vehicleId = vehicleId;
        this.tollPass = tollPass;
    }
}
