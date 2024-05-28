package com.traffic.toll.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity(name = "Toll_Foreign_Vehicle")
public class ForeignVehicle extends Vehicle {

    public ForeignVehicle(Long id, Tag tag) {
        super(id, tag);
    }

    public ForeignVehicle() {
    }
}
