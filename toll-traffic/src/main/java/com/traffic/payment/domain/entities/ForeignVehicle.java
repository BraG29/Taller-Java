package com.traffic.payment.domain.entities;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "Payment_Foreign_Vehicle")
public class ForeignVehicle extends Vehicle {

    public ForeignVehicle(Long id, Tag tag) {
            super(id, tag);
        }

    public ForeignVehicle() {
        }
    }