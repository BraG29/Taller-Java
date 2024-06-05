package com.traffic.payment.domain.repository;


import com.traffic.dtos.account.CreditCardDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.dtos.vehicle.TagDTO;
import com.traffic.dtos.vehicle.VehicleDTO;
import com.traffic.payment.domain.entities.TollPass;
import com.traffic.exceptions.InternalErrorException;
import com.traffic.payment.domain.entities.User;
import com.traffic.payment.domain.entities.Vehicle;

import java.util.ArrayList;
import java.util.List;

public interface PaymentRepository{

    public void initialize();

    public void addUser(User userToAdd);

    public List<User> getAllUsers();

    public User getUserById(Long id);

    public List<TollPass> getAllTollPasses();

    public void addTollPassToUserVehicle(UserDTO userDTO,
                                         VehicleDTO vehicleDTO,
                                         Double amount,
                                         CreditCardDTO creditCardDTO) throws InternalErrorException;

    public Vehicle findVehicleByTag(TagDTO tagDTO) throws InternalErrorException;
}
