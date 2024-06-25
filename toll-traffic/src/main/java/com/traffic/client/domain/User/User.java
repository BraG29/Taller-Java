package com.traffic.client.domain.User;

import com.traffic.client.domain.Account.CreditCard;
import com.traffic.client.domain.Vehicle.Link;
import com.traffic.client.domain.Vehicle.Tag;
import com.traffic.client.domain.Vehicle.TollPass;
import com.traffic.client.domain.Vehicle.Vehicle;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "ClientModule_User")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    private String name;
    @Column(unique = true, nullable = false)
    private String ci;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tollCustomer_id")
    private TollCustomer tollCustomer;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
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


    @Override
    public String toString(){
        return "[ ID: " + id + " Nombre: " + name
                + " Ci: " +  ci + " Email: " + email
                + " Vehiculo/s: " + linkedCars.toString()
                + " Cuenta/s: " + tollCustomer.toString() + " ]";
    }

}
