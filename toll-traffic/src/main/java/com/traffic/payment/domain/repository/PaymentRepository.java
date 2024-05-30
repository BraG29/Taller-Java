package com.traffic.payment.domain.repository;


import com.traffic.dtos.account.CreditCardDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.dtos.vehicle.VehicleDTO;
import com.traffic.payment.domain.entities.User;

import java.util.ArrayList;

public interface PaymentRepository{

    public void initialize();

    public void addUser(User userToAdd);

    public ArrayList<User> getAllUsers();

    public User getUserById(Long id);

    public void addTollPassToUserVehicle(UserDTO userDTO, VehicleDTO vehicleDTO, Double amount, CreditCardDTO creditCardDTO);
}
