package com.traffic.events;

public class VehiclePassEvent extends CustomEvent {

    public VehiclePassEvent() {
    }

    public VehiclePassEvent(String description) {
        super(description);
    }
}
