package com.traffic.dtos.vehicle;

import lombok.Data;

@Data
public class LicensePlateDTO {

    private String licensePlateNumber;

    public LicensePlateDTO() {
    }

    public LicensePlateDTO(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

}
