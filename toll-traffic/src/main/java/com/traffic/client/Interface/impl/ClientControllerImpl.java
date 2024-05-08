package com.traffic.client.Interface.impl;

import com.traffic.client.Interface.ClientController;
import com.traffic.dtos.account.AccountDTO;
import com.traffic.dtos.account.CreditCardDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.dtos.vehicle.TagDTO;
import com.traffic.dtos.vehicle.TollPassDTO;
import com.traffic.dtos.vehicle.VehicleDTO;
import com.traffic.exceptions.*;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ClientControllerImpl implements ClientController {


    @Override
    public void addTollCostumer(UserDTO user) throws IllegalArgumentException {

    }

    @Override
    public void linkVehicle(UserDTO user, VehicleDTO vehicle) throws IllegalArgumentException, NoCustomerException {

    }

    @Override
    public void unLinkVehicle(UserDTO user, VehicleDTO vehicle) throws IllegalArgumentException, InvalidVehicleException, NoCustomerException {

    }

    @Override
    public Optional<List<VehicleDTO>> showLinkedVehicles(UserDTO user) throws IllegalArgumentException, NoCustomerException {
        return Optional.empty();
    }

    @Override
    public void loadBalance(UserDTO user, Double balance) throws IllegalArgumentException, NoCustomerException {

    }

    @Override
    public Optional<Double> showBalance(UserDTO user) throws IllegalArgumentException, NoCustomerException {
        return Optional.empty();
    }

    @Override
    public void linkCreditCard(UserDTO UserDTO, CreditCardDTO creditCard) throws IllegalArgumentException, NoCustomerException {

    }

    @Override
    public Optional<List<TollPassDTO>> showPastPassages(UserDTO user, LocalDate from, LocalDate to) throws IllegalArgumentException, IllegalRangeException, NoCustomerException {
        return Optional.empty();
    }

    @Override
    public Optional<List<TollPassDTO>> showPastPassagesVehicle(UserDTO user, VehicleDTO vehicle, LocalDate from, LocalDate to) throws IllegalArgumentException, IllegalRangeException, NoCustomerException {
        return Optional.empty();
    }

    @Override
    public Optional<List<AccountDTO>> getAccountByTag(TagDTO tag) throws IllegalArgumentException {
        return Optional.empty();
    }

    @Override
    public void prePay(Double balance) throws IllegalArgumentException, NoCustomerException {

    }

    @Override
    public void postPay(Double balance) throws IllegalArgumentException, NoCustomerException {

    }
}
