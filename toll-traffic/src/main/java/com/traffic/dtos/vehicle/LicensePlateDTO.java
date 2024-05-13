package com.traffic.dtos.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LicensePlateDTO extends IdentifierDTO {

    private Long id;
    private String licensePlateNumber;

}
