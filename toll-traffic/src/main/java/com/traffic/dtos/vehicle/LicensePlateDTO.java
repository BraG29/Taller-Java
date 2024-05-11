package com.traffic.dtos.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LicensePlateDTO extends IdentifierDTO {

    private String licensePlateNumber;

}
