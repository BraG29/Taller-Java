package com.traffic.payment.domain.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

//@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Entity
@DiscriminatorValue("national")
public class NationalUser extends User {


    public NationalUser(){}

    public NationalUser(List<Link> linkedCars, TollCustomer tollCustomer,
                        String ci, String name, String password,
                        String email, Long id) {
        super(linkedCars, tollCustomer, ci, name, password, email, id);

    }


    @Override
    public String toString(){
        return super.toString();
    }
}
