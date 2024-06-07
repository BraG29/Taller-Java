package com.traffic.client.application.impl;
import com.traffic.client.application.VehicleService;
import com.traffic.client.domain.User.User;
import com.traffic.client.domain.Vehicle.Link;
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
    public void linkVehicle(Long userId, Vehicle vehicle) {

        try{

            repo.linkVehicle(userId, vehicle);

        }catch (Exception e){
            System.err.println(e.getMessage());
            throw e;
        }


    }

    @Override
    public void unLinkVehicle(Long userId, Long vehicleId) {

        try{
            repo.unLinkVehicle(userId, vehicleId);

        }catch (Exception e){
            System.err.println(e.getMessage());
            throw e;
        }
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

        Optional<User> userOPT = repo.getUserById(id);

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

                            if(tollPass.getPassDate().isAfter(from.minusDays(1)) &&
                                    tollPass.getPassDate().isBefore(to.plusDays(1)))

                            { //si la fecha se encuentra dentro del rango (inclusive).
                                tollPassListInRange.add(tollPass);
                            }
                        }
                        return Optional.of(tollPassListInRange);
                    }
                }
                return Optional.of(tollPassListInRange);
            }else{
                return Optional.empty();
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<List<TollPass>> getTollPassByVehicle(Long tagId, LocalDate from, LocalDate to) {

            Optional<Vehicle> vehicle = repo.getVehicleByTag(tagId);

            if(vehicle.isPresent()){

                List<TollPass> tollPassList = vehicle.get().getTollPass();
                List<TollPass> tollPassListInRange = new ArrayList<>();

                if(tollPassList != null){
                    for(TollPass tollPass: tollPassList){
                        if(tollPass.getPassDate().isAfter(from.minusDays(1)) && tollPass.getPassDate().isBefore(to.plusDays(1))){ //si la fecha se encuentra dentro del rango (inclusive).
                            tollPassListInRange.add(tollPass);
                        }
                    }
                    return Optional.of(tollPassListInRange);

                } else{
                    return Optional.of(tollPassListInRange); //vehiculo sin pasadas.
                }

            }else{
                return Optional.empty(); //vehiculo no encontrado
            }

    }
}
