package com.traffic.dtos.vehicle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class VehicleDTO {

    protected List<TollPassDTO> tollPassDTO;
    protected TagDTO tagDTO;

}
