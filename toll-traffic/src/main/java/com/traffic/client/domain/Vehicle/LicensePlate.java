package com.traffic.client.domain.Vehicle;

import lombok.Data;

@Data
public class LicensePlate {

    private String licensePlateNumber;

    public LicensePlate() {
    }

    public LicensePlate(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }
}
