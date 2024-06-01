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
import java.util.NoSuchElementException;
import java.util.Optional;

@ApplicationScoped
public class VehicleServiceImpl implements VehicleService {

    @Inject
    ClientModuleRepository repo;

    @Override
    public void linkVehicle(Long id, Vehicle vehicle) {

        Optional<User> userOPT = Optional.of(repo.getUserById(id).orElseThrow(() ->
                new NoSuchElementException("No se encontró usuario con id: " + id)));

        User user = userOPT.get();

        user.addVehicle(vehicle); //agrego vehiculo
        repo.update(user); //actualizo lista
    }

    @Override
    public void unLinkVehicle(Long id, Long vehicleId) {
        //TODO implementar
    }

    @Override
    public Optional<List<Vehicle>> getLinkedVehicles(Long id) {

        Optional<User> userOPT  = Optional.of(repo.getUserById(id).orElseThrow(() ->
                new NoSuchElementException("No se encontró el usuario con id: " + id)));

        List<Vehicle> vehicleList = new ArrayList<>();
        Vehicle vehicleObject;

        User user = userOPT.get();

        List<Link> links = user.getLinkedCars();

        if(links != null){
            for(Link link : links){

                vehicleObject = link.getVehicle();
                vehicleList.add(vehicleObject);
            }

            return Optional.of(vehicleList);
        }

        return Optional.empty();
    }

    @Override
    public Optional<List<TollPass>> getTollPass(Long id, LocalDate from, LocalDate to) {

        Optional<User> userOPT = Optional.ofNullable(repo.getUserById(id).orElseThrow(()
        -> new NoSuchElementException("No se encontró el usuario a consultar pasadas con id: " + id)));

        List<TollPass> tollPassList;
        List<TollPass> tollPassListInRange = new ArrayList<>();
        List<Link> linkList;

        if(userOPT.isPresent()){

            User user = userOPT.get();

            if(user.getLinkedCars() != null){

                linkList = user.getLinkedCars();

                for (Link link : linkList){

                    tollPassList = link.getVehicle().getTollPass();

                    if(tollPassList != null){
                        for (TollPass tollPass : tollPassList){

                            if(!tollPass.getPassDate().isAfter(from) && !tollPass.getPassDate().isBefore(to)){ //si la fecha se encuentra dentro del rango (inclusive).
                                tollPassListInRange.add(tollPass);
                            }
                        }
                        return Optional.of(tollPassListInRange);
                    }

                }
            }
        }


        return Optional.empty();
    }

    @Override
    public Optional<List<TollPass>> getTollPassByVehicle(Tag tag, LocalDate from, LocalDate to) {

            Optional<Vehicle> vehicle = Optional.ofNullable(repo.getVehicleByTag(tag).orElseThrow(() ->
                    new NoSuchElementException("No se encontró el vehículo con tag: " + tag.getId())));

            if(vehicle.isPresent()){
                List<TollPass> tollPassList = vehicle.get().getTollPass();
                List<TollPass> tollPassListInRange = new ArrayList<>();

                if(tollPassList != null){
                    for(TollPass tollPass: tollPassList){

                        if(!tollPass.getPassDate().isAfter(from) && !tollPass.getPassDate().isBefore(to)){ //si la fecha se encuentra dentro del rango (inclusive).
                            tollPassListInRange.add(tollPass);
                        }
                    }
                    return Optional.of(tollPassListInRange);
                } else{
                    return Optional.empty();
                }

            }

        return Optional.empty();
    }
}
