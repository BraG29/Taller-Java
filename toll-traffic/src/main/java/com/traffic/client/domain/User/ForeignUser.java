package com.traffic.client.domain.User;

import com.traffic.client.domain.Vehicle.Link;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ForeignUser extends User {


    public ForeignUser() {
    }

    public ForeignUser(List<Link> linkedCars, TollCustomer tollCustomer, String ci, String name, String password, String email, Long id) {
        super(linkedCars, tollCustomer, ci, name, password, email, id);
    }
}
