package com.traffic.toll.domain.repositories;

import com.traffic.dtos.vehicle.LicensePlateDTO;
import com.traffic.dtos.vehicle.TagDTO;
import com.traffic.toll.domain.entities.Vehicle;

import java.util.Optional;

public interface VehicleRepository {

    //TODO: documentar operaciones

    public Optional<Vehicle> findByTag(TagDTO tagDTO);
    public Optional<Vehicle> findByLicensePlate(LicensePlateDTO licensePlateDTO);
    public Optional<Vehicle> update(Vehicle vehicle);

}
