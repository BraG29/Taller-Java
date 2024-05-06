package com.traffic.dtos.vehicle;

import lombok.Data;

import java.util.List;

@Data
public class VehicleDTO {

    protected List<TollPassDTO> tollPassDTO;
    protected TagDTO tagDTO;

    public VehicleDTO() {
    }

    public VehicleDTO(List<TollPassDTO> tollPassDTO, TagDTO tagDTO) {
        this.tollPassDTO = tollPassDTO;
        this.tagDTO = tagDTO;
    }
}
