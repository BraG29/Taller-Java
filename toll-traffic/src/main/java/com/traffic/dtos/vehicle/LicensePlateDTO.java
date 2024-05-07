package com.traffic.dtos.vehicle;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LicensePlateDTO extends IndetifierDTO{

    private String licensePlateNumber;

    public LicensePlateDTO() {
    }

    public LicensePlateDTO(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

}
