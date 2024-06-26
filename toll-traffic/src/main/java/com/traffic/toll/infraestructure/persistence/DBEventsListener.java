package com.traffic.toll.infraestructure.persistence;

import com.traffic.dtos.vehicle.ForeignVehicleDTO;
import com.traffic.dtos.vehicle.NationalVehicleDTO;
import com.traffic.dtos.vehicle.VehicleDTO;
import com.traffic.events.VehicleAddedEvent;
import com.traffic.toll.domain.entities.*;
import com.traffic.toll.domain.repositories.VehicleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.UUID;

@ApplicationScoped
public class DBEventsListener {

    @Inject
    private VehicleRepository vehicleRepository;

    @Transactional
    public void acceptNewVehicle(@Observes VehicleAddedEvent e){

        Vehicle vehicle = null;
        VehicleDTO vehicleDTO = e.getVehicle();
        Tag tag = new Tag(
                vehicleDTO.getTagDTO().getId(),
                UUID.fromString(vehicleDTO
                        .getTagDTO()
                        .getUniqueId()));

        if(vehicleDTO instanceof ForeignVehicleDTO){
            vehicle = new ForeignVehicle(
                    vehicleDTO.getId()
                    ,tag);

        }else{
            NationalVehicleDTO nationalVehicleDTO = (NationalVehicleDTO) vehicleDTO;
            LicensePlate licensePlate = new LicensePlate(
                    vehicleDTO.getId(),
                    nationalVehicleDTO
                            .getLicensePlateDTO()
                            .getLicensePlateNumber());

            vehicle = new NationalVehicle(vehicleDTO.getId(), tag, licensePlate);

        }

        vehicleRepository.save(vehicle);
    }

}
