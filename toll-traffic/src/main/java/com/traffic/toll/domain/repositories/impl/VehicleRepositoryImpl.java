package com.traffic.toll.domain.repositories.impl;

import com.traffic.dtos.vehicle.LicensePlateDTO;
import com.traffic.dtos.vehicle.TagDTO;
import com.traffic.toll.domain.entities.*;
import com.traffic.toll.domain.repositories.VehicleRepository;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class VehicleRepositoryImpl implements VehicleRepository {

    private List<Vehicle> vehicles;

    public VehicleRepositoryImpl() {

        initVehicles();

    }

    @PostConstruct
    public void initVehicles(){
        vehicles = List.of(
                new NationalVehicle(1L,
                        new Tag(100L),
                        new ArrayList<TollPass>(),
                        new LicensePlate(1L, "ABC-123")),
                new ForeignVehicle(2L,
                        new Tag(101L),
                        new ArrayList<TollPass>()),
                new NationalVehicle(3L,
                        new Tag(102L),
                        new ArrayList<TollPass>(),
                        new LicensePlate(2L, "DEF-456")),
                new ForeignVehicle(4L,
                        new Tag(103L),
                        new ArrayList<TollPass>()),
                new NationalVehicle(5L,
                        new Tag(104L),
                        new ArrayList<TollPass>(),
                        new LicensePlate(2L, "GHI-789")),
                new ForeignVehicle(6L,
                        new Tag(105L),
                        new ArrayList<TollPass>())
        );
    }

    @Override
    public Optional<Vehicle> findByTag(TagDTO tagDTO) {

        return vehicles.stream()
                .filter( vehicle -> vehicle.getTag().getUniqueId().equals(tagDTO.getUniqueId()))
                .findFirst();
    }

    @Override
    public Optional<Vehicle> findByLicensePlate(LicensePlateDTO licensePlateDTO) {

        for(Vehicle vehicle : vehicles){
            if(vehicle instanceof NationalVehicle){
                NationalVehicle nationalVehicle = (NationalVehicle) vehicle;
                if(nationalVehicle.getLicensePlate()
                        .getLicensePlateNumber()
                        .equalsIgnoreCase(licensePlateDTO.getLicensePlateNumber())){

                    return Optional.of(nationalVehicle);
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public void update(Vehicle vehicle) {

        Optional<Vehicle> vehicleOPT = vehicles.stream()
                .filter(v -> v.getId().equals(vehicle.getId()))
                .findFirst();

        if(vehicleOPT.isPresent()){
            Vehicle savedVehicle = vehicleOPT.get();

            savedVehicle.updateEntity(vehicle);

        }
    }
}
