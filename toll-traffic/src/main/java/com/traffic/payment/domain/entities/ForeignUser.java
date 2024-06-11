package com.traffic.payment.domain.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

//@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Entity(name="Payment_ForeignUser")
@DiscriminatorValue("foreign")
public class ForeignUser extends User {


    public ForeignUser() {
    }

    public ForeignUser(List<Link> linkedCars, TollCustomer tollCustomer, String ci, String name, String password, String email, Long id) {
        super(linkedCars, tollCustomer, ci, name, password, email, id);
    }

    @Override
    public String toString(){
        return super.toString();
    }
}