package com.traffic.client.domain.Vehicle;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
//
//@EqualsAndHashCode(callSuper = true)
//@Data
@Getter
@Setter
@Entity
@DiscriminatorValue("foreign")
public class ForeignVehicle extends Vehicle {

    public ForeignVehicle() {
    }

    public ForeignVehicle(Long id, Tag tag, List<TollPass> tollPass) {
        super(id, tag, tollPass);
    }


//    @Override
//    public String toString(){
//        return super.toString() + " ]";
//    }
}
