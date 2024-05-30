package com.traffic.client.domain.Vehicle;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "ClientModule_LicensePlate")
public class LicensePlate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
