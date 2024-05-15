package com.traffic.client.domain.repository;


import com.traffic.client.domain.Account.CreditCard;
import com.traffic.client.domain.User.User;
import com.traffic.client.domain.Vehicle.Tag;
import com.traffic.client.domain.Vehicle.Vehicle;

import java.util.List;
import java.util.Optional;

/**
 *
 * Este archivo de repositorio cumplirá un funcionamiento similiar a los DAOS(Data Access Object),
 * para proporcionar metodos que interactuen con la capa de persistencia de la base de datos, los cuales
 * se llamarán desde la implementación de la capa de aplicacion.
 *
 *
 */
public interface ClientModuleRepository {

    /**
     * Encuentra un usuario utilizando el tag del vehiculo, en un futuro realizará consultas a una bd.
     * @param tag -> tag del vehiculo del usuario.
     * @return -> un objeto Usuario.
     */
    public Optional<User> findByTag(Tag tag);

    public Optional<List<User>> listUsers();

    public void createUser(User user);

    public Optional<User> getUserById(Long id);

    public Optional<Vehicle> getVehicleByTag(Tag tag);

    public void usersInit();

    public void linkVehicle(User usr, Vehicle vehicle);

    public void unLinkVehicle(User usr, Vehicle vehicle);

    public Optional<List<Vehicle>> showLinkedVehicles(User usr);

    public void linkCreditCard(User usr, CreditCard card);

    public void loadBalance(User usr, Double balance);

    public Optional<Double> showBalance(User usr);

    public void payPrePay(User usr, Double balance);

    //pasadas
}
