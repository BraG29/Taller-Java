package com.traffic.dtos.user;

import com.traffic.dtos.vehicle.LinkDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
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
