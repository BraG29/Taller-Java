package com.traffic.sucive.domain.entities;

import com.traffic.dtos.vehicle.LicensePlateDTO;
import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Sucive_License_Plate")
@ToString

public class LicensePlate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "license_plate_number")
    private String licensePlateNumber;

    public LicensePlateDTO toDTO(){
        return new LicensePlateDTO(this.getId(), this.getLicensePlateNumber());
    }
}