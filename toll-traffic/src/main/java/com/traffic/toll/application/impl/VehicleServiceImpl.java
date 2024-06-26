package com.traffic.toll.application.impl;

import com.traffic.dtos.vehicle.IdentifierDTO;
import com.traffic.dtos.vehicle.LicensePlateDTO;
import com.traffic.toll.application.VehicleService;
import com.traffic.toll.domain.entities.LicensePlate;
import com.traffic.toll.domain.entities.Tag;
import com.traffic.toll.domain.entities.Vehicle;
import com.traffic.toll.domain.repositories.IdentifierRepository;
import com.traffic.toll.domain.repositories.VehicleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class VehicleServiceImpl implements VehicleService {

    @Inject
    private VehicleRepository vehicleRepository;
    @Inject
    private IdentifierRepository identifierRepository;

    @Override
    public Optional<Vehicle> getByIdentifier(IdentifierDTO identifier) {

        if(identifier instanceof LicensePlateDTO){
            Optional<LicensePlate> licensePlateOPT = identifierRepository.findLicensePlateById(
                    identifier.getId());

            return licensePlateOPT.flatMap(licensePlate ->
                    vehicleRepository.findByLicensePlate(licensePlate));
        } else{
            Optional<Tag> tagOPT = identifierRepository.findTagById(
                    identifier.getId());

            return tagOPT.flatMap(tag ->
                    vehicleRepository.findByTag(tag));
        }
    }
}
