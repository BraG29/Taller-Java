package com.traffic.sucive.domain.repository;

import com.traffic.dtos.vehicle.LicensePlateDTO;
import com.traffic.exceptions.InvalidVehicleException;
import com.traffic.sucive.domain.entities.NationalVehicle;
import com.traffic.sucive.domain.entities.User;

import java.util.List;

public interface SuciveRepository {

    public void addUser(User user);

    public List<User> getAllUsers();

    public NationalVehicle findVehicleByLicensePlate(LicensePlateDTO licensePlateDTO);

    public void updateVehicleTollPass(LicensePlateDTO licensePlateDTO, Double amount) throws InvalidVehicleException;
    }
