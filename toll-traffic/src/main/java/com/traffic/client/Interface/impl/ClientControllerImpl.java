package com.traffic.client.Interface.impl;

import com.traffic.client.Interface.ClientController;
import com.traffic.dtos.account.AccountDTO;
import com.traffic.dtos.account.CreditCardDTO;
import com.traffic.dtos.user.TollCustomerDTO;
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
    public void addTollCostumer(UserDTO user) throws NoUserException {

    }

    @Override
    public void linkVehicle(TollCustomerDTO tollCustomer, VehicleDTO vehicle) throws NoCustomerException, InvalidVehicleException {

    }

    @Override
    public void unLinkVehicle(TollCustomerDTO tollCustomer, VehicleDTO vehicle) throws NoCustomerException, InvalidVehicleException {

    }

    @Override
    public Optional<List<VehicleDTO>> showLinkedVehicles(TollCustomerDTO tollCustomer) throws NoCustomerException {
        return Optional.empty();
    }

    @Override
    public void loadBalance(TollCustomerDTO tollCustomer, Double balance) throws NoCustomerException, IllegalArgumentException {

    }

    @Override
    public Optional<Double> showBalance(TollCustomerDTO tollCustomer) throws NoCustomerException {
        return Optional.empty();
    }

    @Override
    public void linkCreditCard(TollCustomerDTO tollCustomerDTO, CreditCardDTO creditCard) throws NoCustomerException {

    }

    @Override
    public Optional<List<TollPassDTO>> showPastPassages(TollCustomerDTO tollCustomer, LocalDate dateStart, LocalDate endDate) throws NoCustomerException, IllegalRangeException {
        return Optional.empty();
    }

    @Override
    public Optional<List<TollPassDTO>> showPastPassagesVehicle(TollCustomerDTO tollCustomer, VehicleDTO vehicle, LocalDate dateStart, LocalDate endDate) throws NoCustomerException, IllegalRangeException {
        return Optional.empty();
    }

    @Override
    public Optional<List<AccountDTO>> getAccountByTag(TagDTO tag) throws NoTagException {
        return Optional.empty();
    }

    @Override
    public void prePay(Double balance) throws IllegalArgumentException {

    }

    @Override
    public void postPay(Double balance) throws IllegalArgumentException {

    }
}
