package com.traffic.client.application;

import com.traffic.client.domain.Vehicle.Tag;
import com.traffic.client.domain.Vehicle.TollPass;
import com.traffic.client.domain.Vehicle.Vehicle;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VehicleService {

    public void linkVehicle(Long id, Vehicle vehicle);

    public void unLinkVehicle (Long id, Vehicle vehicle);

    public Optional<List<Vehicle>> getLinkedVehicles(Long id);

    public Optional<List<TollPass>> getTollPass(Long id, LocalDate from, LocalDate to);

    public Optional<List<TollPass>> getTollPassByVehicle(Tag tag, LocalDate from, LocalDate to);

}
