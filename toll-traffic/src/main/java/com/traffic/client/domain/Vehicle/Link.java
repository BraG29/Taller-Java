package com.traffic.client.domain.Vehicle;

import com.traffic.client.domain.User.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

//@Data
@Getter
@Setter
@Entity(name = "ClientModule_Link")
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean active;

    @OneToOne
    @JoinColumn(name = "Vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    private User user;

    private LocalDate initialDate;

    public Link(){}

    public Link(Long id,Boolean active, Vehicle vehicle, LocalDate initialDate) {
        this.id = id;
        this.active = active;
        this.vehicle = vehicle;
        this.initialDate = initialDate;
    }

//    @Override
//    public String toString(){
//        return "Vinculo: [" + id + " activo?: " + active + " Vehiculo: " +
//                vehicle.toString() + " Fecha vinculación: " + initialDate.toString();
//    }
}
