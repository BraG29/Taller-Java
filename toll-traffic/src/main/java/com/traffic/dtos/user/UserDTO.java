package com.traffic.dtos.user;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.traffic.dtos.vehicle.LinkDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = NationalUserDTO.class, name = "national"),
        @JsonSubTypes.Type(value = ForeignUserDTO.class, name = "foreign")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class UserDTO {

    protected Long id;
    protected String email;
    protected String password;
    protected String name;
    protected String ci;
    protected TollCustomerDTO tollCustomer;
    protected List<LinkDTO> linkedVehicles;
    protected List<NotificationDTO> notifications;

}
