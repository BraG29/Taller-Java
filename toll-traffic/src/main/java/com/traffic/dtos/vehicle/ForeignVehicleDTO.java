package com.traffic.dtos.vehicle;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
public class ForeignVehicleDTO extends VehicleDTO {

    public ForeignVehicleDTO() {
    }

    public ForeignVehicleDTO(List<TollPassDTO> tollPassDTO, TagDTO tagDTO) {
        super(tollPassDTO, tagDTO);
    }
}
