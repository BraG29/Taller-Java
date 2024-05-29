package com.traffic.client.domain.repository;


import com.traffic.client.domain.Account.Account;
import com.traffic.client.domain.Account.CreditCard;
import com.traffic.client.domain.User.User;
import com.traffic.client.domain.Vehicle.Tag;
import com.traffic.client.domain.Vehicle.Vehicle;
import com.traffic.exceptions.NoCustomerException;

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

    /**
     * Lista de usersList del sistema.
     * @return -> devuelve una lista con todos los usersList del sistema.
     */
    public Optional<List<User>> listUsers();

    /**
     * Crea un usuario.
     * @param user -> Recibe el usuario a crear.
     */
    public void createUser(User user);

    /**
     * Busca y devuelve un usuario en particular.
     * @param id -> identificador del usuario a buscar.
     * @return -> devuelve un usuario.
     */
    public Optional<User> getUserById(Long id);

    /**
     * Busca y devuelve un vehiculo en particular.
     * @param tag -> Recibe el tag del vehiculo a buscar.
     * @return -> devuelve un vehiculo.
     */
    public Optional<Vehicle> getVehicleByTag(Tag tag);


    /**
     * Actualiza cambios de un usuario en una lista.
     * @param usr -> recibe el usuario a actualizar en la lista.
     */
    public void update(User usr);

    public Optional<List<Account>> getAccountsByTag(Long id);

    public void loadBalance(Long tagId, Double balance) throws Exception;

    public void prePay(Long tagId, Double balance) throws Exception;

    public void postPay(Long tagId, Double cost) throws Exception;
}
