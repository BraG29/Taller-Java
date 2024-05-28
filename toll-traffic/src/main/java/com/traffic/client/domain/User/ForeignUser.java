package com.traffic.client.domain.User;

import com.traffic.client.domain.Vehicle.Link;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
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
