package com.traffic.dtos.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ForeignVehicleDTO extends VehicleDTO {

    public ForeignVehicleDTO(Long id, List<TollPassDTO> tollPassDTO, TagDTO tagDTO) {
        super(id, tollPassDTO, tagDTO);
    }
}
