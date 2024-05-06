package com.traffic.dtos.vehicle;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class NationalVehicleDTO extends VehicleDTO{

    private LicensePlateDTO licensePlateDTO;

    public NationalVehicleDTO() {
    }

    public NationalVehicleDTO(List<TollPassDTO> tollPassDTO, TagDTO tagDTO, LicensePlateDTO licensePlateDTO) {
        super(tollPassDTO, tagDTO);
        this.licensePlateDTO = licensePlateDTO;
    }
}
