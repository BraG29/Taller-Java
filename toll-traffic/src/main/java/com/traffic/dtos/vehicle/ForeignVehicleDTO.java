package com.traffic.dtos.vehicle;

import lombok.Getter;

import java.util.List;

@Getter
public class ForeignVehicleDTO extends VehicleDTO {

    public ForeignVehicleDTO(Long id, List<TollPassDTO> tollPassDTO, TagDTO tagDTO) {
        super(id, tollPassDTO, tagDTO);
    }
}
