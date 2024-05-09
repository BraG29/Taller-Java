package com.traffic.client.domain.User;

import com.traffic.client.domain.Vehicle.Link;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class NationalUser extends User{

    public NationalUser(){}

    public NationalUser(List<Link> linkedCars, TollCustomer tollCustomer, String ci, String name, String password, String email, Long id) {
        super(linkedCars, tollCustomer, ci, name, password, email, id);
    }
}
