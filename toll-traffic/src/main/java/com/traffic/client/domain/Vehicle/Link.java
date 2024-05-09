package com.traffic.client.domain.Vehicle;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Link {

    private Boolean active;
    private Vehicle vehicle;
    private LocalDate initialDate;

    public Link(){}

    public Link(Boolean active, Vehicle vehicle, LocalDate initialDate) {
        this.active = active;
        this.vehicle = vehicle;
        this.initialDate = initialDate;
    }
}
