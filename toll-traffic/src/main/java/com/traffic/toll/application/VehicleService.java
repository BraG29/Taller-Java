package com.traffic.toll.application;

import com.traffic.dtos.vehicle.IdentifierDTO;
import com.traffic.toll.domain.entities.Vehicle;

import java.util.Optional;

public interface VehicleService {

    public Optional<Vehicle> getByIdentifier(IdentifierDTO identifier);

}
