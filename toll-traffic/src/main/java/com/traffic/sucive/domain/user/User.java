package com.traffic.sucive.domain.user;

import com.traffic.sucive.domain.user.TollCustomer;
import com.traffic.sucive.domain.vehicle.Link;
import lombok.Data;
import java.util.List;


//this annotation auto generates setters & getters, I think
@Data
public class User {

    protected Long id;
    protected String email;
    protected String password;
    protected String name;
    protected String ci;
    protected TollCustomer tollCustomer;
    protected List<Link> linkedVehicles;

    public User() {
        super();
    }

    public User(Long id,
                String email,
                String password,
                String name,
                String ci,
                TollCustomer tollCustomer,
                List<Link> linkedVehicles) {

        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.ci = ci;
        this.tollCustomer = tollCustomer;
        this.linkedVehicles = linkedVehicles;
    }
}
