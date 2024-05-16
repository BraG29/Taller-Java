package com.traffic.client.application.impl;

import com.traffic.client.application.VehicleService;
import com.traffic.client.domain.User.User;
import com.traffic.client.domain.Vehicle.Link;
import com.traffic.client.domain.Vehicle.Tag;
import com.traffic.client.domain.Vehicle.TollPass;
import com.traffic.client.domain.Vehicle.Vehicle;
import com.traffic.client.domain.repository.ClientModuleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class VehicleServiceImpl implements VehicleService {

    @Inject
    ClientModuleRepository repo;

    @Override
    public void linkVehicle(Long id, Vehicle vehicle) {

        User user = repo.getUserById(id);

        try{
            user.addVehicle(vehicle);
        } catch(Exception ignored) {

        }

    }

    @Override
    public void unLinkVehicle(Long id, Vehicle vehicle) {

        User user = repo.getUserById(id);

        try{
            user.removeVehicle(vehicle);
        } catch(Exception ignored) {

        }

    }

    @Override
    public Optional<List<Vehicle>> getLinkedVehicles(Long id) {

        User user = repo.getUserById(id);

        List<Vehicle> vehicleList = new ArrayList<>();
        Vehicle vehicleObject = null;

        List<Link> links = null;

        if(user != null){

            links = user.getLinkedCars();

            if(links != null){
                for(Link link : links){

                    vehicleObject = link.getVehicle();
                    vehicleList.add(vehicleObject);
                }

                return Optional.of(vehicleList);
            }

        }

        return Optional.empty();
    }

    @Override
    public Optional<List<TollPass>> getTollPass(Long id, LocalDate from, LocalDate to) {

        User user = repo.getUserById(id);

        List<TollPass> tollPassList;
        List<TollPass> tollPassListInRange = new ArrayList<>();
        List<Link> linkList = null;

        if(user != null && user.getLinkedCars() != null){

            linkList = user.getLinkedCars();

            for (Link link : linkList){

               tollPassList = link.getVehicle().getTollPass();

               for (TollPass tollPass : tollPassList){

                   if(tollPass.getPassDate().isAfter(from) && tollPass.getPassDate().isBefore(to)){ //si la fecha se encuentra dentro del rango.
                       tollPassListInRange.add(tollPass);
                   }
               }
               return Optional.of(tollPassListInRange);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<TollPass>> getTollPassByVehicle(Tag tag, LocalDate from, LocalDate to) {

        if(tag != null){
            Vehicle vehicle = repo.getVehicleByTag(tag);

            List<TollPass> tollPassList = vehicle.getTollPass();
            List<TollPass> tollPassListInRange = new ArrayList<>();

            for(TollPass tollPass: tollPassList){

                if(tollPass.getPassDate().isAfter(from) && tollPass.getPassDate().isBefore(to)){ //si la fecha se encuentra dentro del rango.
                    tollPassListInRange.add(tollPass);
                }
            }
            return Optional.of(tollPassListInRange);
        }
        return Optional.empty();
    }
}
