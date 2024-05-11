package com.traffic.dtos.vehicle;

import lombok.Getter;

import java.util.List;

@Getter
public class NationalVehicleDTO extends VehicleDTO{

    private LicensePlateDTO licensePlateDTO;

    public NationalVehicleDTO(Long id, List<TollPassDTO> tollPassDTO, TagDTO tagDTO, LicensePlateDTO licensePlateDTO) {
        super(id, tollPassDTO, tagDTO);
        this.licensePlateDTO = licensePlateDTO;
    }
}
