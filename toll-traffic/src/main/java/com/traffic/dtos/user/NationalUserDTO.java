package com.traffic.dtos.user;

import com.traffic.dtos.vehicle.LinkDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class NationalUserDTO extends UserDTO{

    private SuciveCustomerDTO suciveCustomer;

    public NationalUserDTO() {
        super();
    }

    public NationalUserDTO(Long id,
                           String email,
                           String password,
                           String name,
                           String ci,
                           TollCustomerDTO tollCustomer,
                           List<LinkDTO> linkedVehicles,
                           SuciveCustomerDTO suciveCustomer) {
        super(id, email, password, name, ci, tollCustomer, linkedVehicles);
        this.suciveCustomer = suciveCustomer;
    }
}
