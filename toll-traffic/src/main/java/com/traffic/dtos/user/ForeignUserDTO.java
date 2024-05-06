package com.traffic.dtos.user;

import com.traffic.dtos.vehicle.LinkDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ForeignUserDTO extends UserDTO{

    public ForeignUserDTO() {
        super();
    }

    public ForeignUserDTO(Long id,
                          String email,
                          String password,
                          String name,
                          String ci,
                          TollCustomerDTO tollCustomer,
                          List<LinkDTO> linkedVehicles) {
        super(id, email, password, name, ci, tollCustomer, linkedVehicles);
    }
}
