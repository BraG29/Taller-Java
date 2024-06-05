package com.traffic.dtos.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LicensePlateDTO extends IdentifierDTO {

    private String licensePlateNumber;

    public LicensePlateDTO() {
        super();
    }

    public LicensePlateDTO(Long id, String licensePlateNumber) {
        super(id);
        this.licensePlateNumber = licensePlateNumber;
    }
}
