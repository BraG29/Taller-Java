package com.traffic.dtos.user;

import com.traffic.dtos.vehicle.LinkDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public abstract class UserDTO {

    protected Long id;
    protected String email;
    protected String password;
    protected String name;
    protected String ci;
    protected TollCustomerDTO tollCustomer;
    protected List<LinkDTO> linkedVehicles;

    public UserDTO() {
        super();
    }

    public UserDTO(Long id,
                   String email,
                   String password,
                   String name,
                   String ci,
                   TollCustomerDTO tollCustomer,
                   List<LinkDTO> linkedVehicles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.ci = ci;
        this.tollCustomer = tollCustomer;
        this.linkedVehicles = linkedVehicles;
    }
}
