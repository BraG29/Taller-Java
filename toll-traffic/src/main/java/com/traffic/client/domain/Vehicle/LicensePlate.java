package com.traffic.client.domain.Vehicle;

import lombok.Data;

@Data
public class LicensePlate {

    private Long id;
    private String licensePlateNumber;

    public LicensePlate() {
    }

    public LicensePlate(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    @Override
    public String toString(){
        return "Matricula: [" + licensePlateNumber + " ]";
    }
}
