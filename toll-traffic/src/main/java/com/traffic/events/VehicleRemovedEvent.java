package com.traffic.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleRemovedEvent extends CustomEvent{

    private Long vehicleId;

}
