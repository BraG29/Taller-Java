package com.traffic.dtos.vehicle;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.traffic.dtos.user.ForeignUserDTO;
import com.traffic.dtos.user.NationalUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = NationalVehicleDTO.class, name = "national"),
        @JsonSubTypes.Type(value = ForeignVehicleDTO.class, name = "foreign")
})
@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class VehicleDTO {

    private Long id;
    protected List<TollPassDTO> tollPassDTO;
    protected TagDTO tagDTO;

}
