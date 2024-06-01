package com.traffic.dtos.vehicle;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TagDTO.class, name = "tag"),
        @JsonSubTypes.Type(value = LicensePlateDTO.class, name = "licensePlate")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class IdentifierDTO {

    protected Long id;

}
