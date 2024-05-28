package com.traffic.toll.domain.repositories;

import com.traffic.dtos.vehicle.LicensePlateDTO;
import com.traffic.dtos.vehicle.TagDTO;
import com.traffic.toll.domain.entities.LicensePlate;
import com.traffic.toll.domain.entities.Tag;
import com.traffic.toll.domain.entities.Vehicle;

import java.util.Optional;

public interface VehicleRepository{

    //TODO: documentar operaciones
    public Optional<Vehicle> findByTag(Tag tag);
    public Optional<Vehicle> findByLicensePlate(LicensePlate licensePlate);

}
