package com.traffic.dtos.vehicle;

import lombok.Getter;

import java.util.List;

@Getter
public class ForeignVehicleDTO extends VehicleDTO {

    public ForeignVehicleDTO(List<TollPassDTO> tollPassDTO, TagDTO tagDTO) {
        super(tollPassDTO, tagDTO);
    }
}
