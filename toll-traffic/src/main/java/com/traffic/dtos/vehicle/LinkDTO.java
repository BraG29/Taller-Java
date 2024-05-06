package com.traffic.dtos.vehicle;

import lombok.Data;

import java.time.LocalDate;


@Data
public class LinkDTO {

    private LocalDate initialDate;
    private Boolean active;

    public LinkDTO() {
    }

    public LinkDTO(LocalDate initialDate, Boolean active) {
        this.initialDate = initialDate;
        this.active = active;
    }
}
