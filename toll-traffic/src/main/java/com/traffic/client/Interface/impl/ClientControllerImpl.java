package com.traffic.client.Interface.impl;

import com.traffic.client.Interface.ClientController;
import com.traffic.client.application.AccountService;
import com.traffic.client.domain.Account.Account;
import com.traffic.client.domain.Vehicle.Tag;
import com.traffic.dtos.account.AccountDTO;
import com.traffic.dtos.account.CreditCardDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.dtos.vehicle.TagDTO;
import com.traffic.dtos.vehicle.TollPassDTO;
import com.traffic.dtos.vehicle.VehicleDTO;
import com.traffic.exceptions.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ClientControllerImpl implements ClientController {

    @Inject
    private AccountService accountService; //operaciones de la cuenta.

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
    public Optional<List<AccountDTO>> getAccountByTag(TagDTO tagDTO) throws IllegalArgumentException {

        Tag tag = new Tag(tagDTO.getUniqueId());

        Optional<List<Account>> accounts = accountService.getAccountByTag(tag);

        List<AccountDTO> accountsDTO = new ArrayList<>();

        if(accounts.isPresent()){//si hay valores dentro del optional.

            List<Account> accountsList = accounts.get(); //transformo el optional a una lista comun para obtener los elementos.

            for (Account account : accountsList) {

                AccountDTO accDTO = new AccountDTO(account.getAccountNumber(),
                        account.getCreationDate()); //convierto de Account a AccountDTO

                accountsDTO.add(accDTO); //añado a la lista
            }

            return Optional.of(accountsDTO);
        }

        return Optional.empty();
    }

    @Override
    public void prePay(Double balance, TagDTO tagDTO) throws IllegalArgumentException, NoCustomerException {

    }

    @Override
    public void postPay(Double balance, TagDTO tagDTO) throws IllegalArgumentException, NoCustomerException {

    }
}
