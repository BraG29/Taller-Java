package com.traffic.client.domain.Vehicle;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Link {

    private Long id;
    private Boolean active;
    private Vehicle vehicle;
    private LocalDate initialDate;

    public Link(){}

    public Link(Long id,Boolean active, Vehicle vehicle, LocalDate initialDate) {
        this.id = id;
        this.active = active;
        this.vehicle = vehicle;
        this.initialDate = initialDate;
    }

    @Override
    public String toString(){
        return "Vinculo: [" + id + " activo?: " + active + " Vehiculo: " +
                vehicle.toString() + " Fecha vinculación: " + initialDate.toString();
    }
}
