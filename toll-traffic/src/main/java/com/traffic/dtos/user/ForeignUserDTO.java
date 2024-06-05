package com.traffic.dtos.user;

import com.traffic.dtos.vehicle.LinkDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter

public class ForeignUserDTO extends UserDTO{

    public ForeignUserDTO(){}

    public ForeignUserDTO(Long id,
                          String email,
                          String password,
                          String name,
                          String ci,
                          TollCustomerDTO tollCustomer,
                          List<LinkDTO> linkedVehicles,
                          List<NotificationDTO> notifications) {
        super(id, email, password, name, ci, tollCustomer, linkedVehicles, notifications);
    }
}
