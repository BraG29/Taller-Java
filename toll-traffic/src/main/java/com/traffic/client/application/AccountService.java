package com.traffic.client.application;

import com.traffic.client.domain.Account.Account;
import com.traffic.client.domain.Account.CreditCard;
import com.traffic.client.domain.User.User;
import com.traffic.client.domain.Vehicle.Tag;
import com.traffic.exceptions.ExternalApiException;
import com.traffic.exceptions.InvalidVehicleException;
import com.traffic.exceptions.NoCustomerException;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    public void prePay(Tag tag, Double cost) throws NoCustomerException;

    public void postPay(Tag tag, Double cost) throws ExternalApiException, InvalidVehicleException, NoCustomerException;

    public Optional<List<Account>> getAccountByTag(Tag tag);

    public void loadBalance(Long id, Double balance);

    public Optional<Double> showBalance(Long id);

    /**
     * Operacion para vincular una tarjeta de credito a un usuario.
     * @param id -> recibo un id del usuario, desde aqui llamo al repositorio para encontrar el usuario.
     * @param creditCard -> recibo el objeto de la tarjeta.
     */
    public void linkCreditCard(Long id, CreditCard creditCard);
}
