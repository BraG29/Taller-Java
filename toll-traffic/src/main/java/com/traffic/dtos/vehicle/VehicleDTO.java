package com.traffic.dtos.vehicle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public abstract class VehicleDTO {

    private Long id;
    protected List<TollPassDTO> tollPassDTO;
    protected TagDTO tagDTO;

}
