package com.traffic.dtos.vehicle;

import lombok.Data;

import java.time.LocalDate;


@Data
public class LinkDTO {

    private LocalDate initialDate;
    private Boolean active;
    private VehicleDTO vehicle;

    public LinkDTO() {
    }

    public LinkDTO(LocalDate initialDate, Boolean active, VehicleDTO vehicle) {
        this.initialDate = initialDate;
        this.active = active;
        this.vehicle = vehicle;
    }
}
