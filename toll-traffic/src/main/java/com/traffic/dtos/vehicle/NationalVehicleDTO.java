package com.traffic.dtos.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class NationalVehicleDTO extends VehicleDTO{

    private LicensePlateDTO licensePlateDTO;

    public NationalVehicleDTO(Long id, List<TollPassDTO> tollPassDTO, TagDTO tagDTO, LicensePlateDTO licensePlateDTO) {
        super(id, tollPassDTO, tagDTO);
        this.licensePlateDTO = licensePlateDTO;
    }
}
