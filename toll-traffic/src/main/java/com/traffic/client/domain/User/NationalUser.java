package com.traffic.client.domain.User;

import com.traffic.client.domain.Vehicle.Link;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@DiscriminatorValue("national")
public class NationalUser extends User{

    @OneToOne
    @JoinColumn(name = "suciveCustomer_id")
    private SuciveCustomer suciveCustomer;

    public NationalUser(){}

    public NationalUser(List<Link> linkedCars, TollCustomer tollCustomer,
                        String ci, String name, String password,
                        String email, Long id, SuciveCustomer suciveCustomer) {
        super(linkedCars, tollCustomer, ci, name, password, email, id);
        this.suciveCustomer = suciveCustomer;

    }


    @Override
    public String toString(){
        return super.toString();
    }
}
