package com.traffic.toll.domain.entities;

import com.traffic.dtos.vehicle.VehicleDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;


@Getter
@AllArgsConstructor
public class LinkDTO {

    private LocalDate initialDate;
    private Boolean active;
    private VehicleDTO vehicle;

}
