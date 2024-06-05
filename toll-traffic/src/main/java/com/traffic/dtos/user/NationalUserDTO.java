package com.traffic.dtos.user;

import com.traffic.dtos.vehicle.LinkDTO;
import lombok.*;

import java.util.List;

@Getter
public class NationalUserDTO extends UserDTO{

    private SuciveCustomerDTO suciveCustomer;

    public NationalUserDTO(){}

    public NationalUserDTO(Long id,
                           String email,
                           String password,
                           String name,
                           String ci,
                           TollCustomerDTO tollCustomer,
                           List<LinkDTO> linkedVehicles,
                           SuciveCustomerDTO suciveCustomer,
                           List<NotificationDTO> notifications) {
        super(id, email, password, name, ci, tollCustomer, linkedVehicles, notifications);
        this.suciveCustomer = suciveCustomer;
    }
}
