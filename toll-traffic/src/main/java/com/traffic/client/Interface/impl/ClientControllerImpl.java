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
    public void linkVehicle(Long id, VehicleDTO vehicle) throws IllegalArgumentException, NoCustomerException {

    }

    @Override
    public void unLinkVehicle(Long id, VehicleDTO vehicle) throws IllegalArgumentException, InvalidVehicleException, NoCustomerException {

    }

    @Override
    public Optional<List<VehicleDTO>> showLinkedVehicles(Long id) throws IllegalArgumentException, NoCustomerException {
        return Optional.empty();
    }

    @Override
    public void loadBalance(Long id, Double balance) throws IllegalArgumentException, NoCustomerException {

    }

    @Override
    public Optional<Double> showBalance(Long id) throws IllegalArgumentException, NoCustomerException {
        return Optional.empty();
    }

    @Override
    public void linkCreditCard(Long id, CreditCardDTO creditCard) throws IllegalArgumentException, NoCustomerException {

    }

    @Override
    public Optional<List<TollPassDTO>> showPastPassages(Long id, LocalDate from, LocalDate to) throws IllegalArgumentException, IllegalRangeException, NoCustomerException {
        return Optional.empty();
    }

    @Override
    public Optional<List<TollPassDTO>> showPastPassagesVehicle(TagDTO tag, LocalDate from, LocalDate to) throws IllegalArgumentException, IllegalRangeException, NoCustomerException {
        return Optional.empty();
    }

    @Override
    public Optional<List<AccountDTO>> getAccountByTag(TagDTO tagDTO) throws IllegalArgumentException {
        return Optional.empty();
    }

    @Override
    public void prePay(Double balance, TagDTO tagDTO) throws IllegalArgumentException, NoCustomerException {

    }

    @Override
    public void postPay(Double balance, TagDTO tagDTO) throws IllegalArgumentException, NoCustomerException {

    }
}
