package com.traffic.dtos.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;


@Getter
@AllArgsConstructor
public class LinkDTO {

    private Long id;
    private LocalDate initialDate;
    private Boolean active;
    private VehicleDTO vehicle;

}
