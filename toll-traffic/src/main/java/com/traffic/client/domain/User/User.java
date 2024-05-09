package com.traffic.client.domain.User;

import com.traffic.client.domain.Vehicle.Link;
import lombok.Data;

import java.util.List;

@Data
public class User {

    private Long id;
    private String email;
    private String password;
    private String name;
    private String ci;
    private TollCustomer tollCustomer;
    private List<Link> linkedCars;

    public User(){}

    public User(List<Link> linkedCars, TollCustomer tollCustomer, String ci, String name, String password, String email, Long id) {
        this.linkedCars = linkedCars;
        this.tollCustomer = tollCustomer;
        this.ci = ci;
        this.name = name;
        this.password = password;
        this.email = email;
        this.id = id;
    }
}
