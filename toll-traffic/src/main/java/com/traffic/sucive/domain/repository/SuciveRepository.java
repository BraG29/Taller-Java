package com.traffic.sucive.domain.repository;

import com.traffic.dtos.vehicle.LicensePlateDTO;
import com.traffic.dtos.vehicle.VehicleDTO;
import com.traffic.exceptions.InternalErrorException;
import com.traffic.exceptions.InvalidVehicleException;
import com.traffic.sucive.domain.entities.NationalVehicle;
import com.traffic.sucive.domain.entities.TollPass;
import com.traffic.sucive.domain.entities.User;

import java.util.List;

public interface SuciveRepository {

    public void addVehicle(NationalVehicle vehicleToAdd) throws Exception;

    public void addUser(User user);

    public List<TollPass> getAllTollPasses();

    public NationalVehicle findVehicleByLicensePlate(LicensePlateDTO licensePlateDTO) throws InternalErrorException;

    public void updateVehicleTollPass(LicensePlateDTO licensePlateDTO, Double amount) throws InvalidVehicleException, InternalErrorException;

    public void publishPayment(TollPass tollPass, VehicleDTO vehicle );

}
