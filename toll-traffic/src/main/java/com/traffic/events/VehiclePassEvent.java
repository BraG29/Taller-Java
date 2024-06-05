package com.traffic.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VehiclePassEvent extends CustomEvent {

    private String message;



}
