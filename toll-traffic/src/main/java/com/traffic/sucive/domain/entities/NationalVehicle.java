package com.traffic.sucive.domain.entities;

import com.traffic.dtos.vehicle.NationalVehicleDTO;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "Sucive_National_Vehicle")
@DiscriminatorValue("national")
public class NationalVehicle extends Vehicle {

    @OneToOne
    @JoinColumn(name = "LicencePlate_id")
    private LicensePlate plate;

    public NationalVehicle() {}
    public NationalVehicle(Long id, Tag tag, List<TollPass> tollPass, LicensePlate plate) {
        super(id, tag, tollPass);
        this.plate = plate;
    }
    @Override
    public String toString(){
        return super.toString() + plate.toString();
    }

    public NationalVehicleDTO toDTO(){
        return new NationalVehicleDTO(this.getId(),null, this.getTag().toDTO(), this.plate.toDTO());
    }
}