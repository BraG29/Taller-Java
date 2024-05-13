package com.traffic.client.domain.repository;


import com.traffic.client.domain.User.User;
import com.traffic.client.domain.Vehicle.Tag;
import com.traffic.client.domain.Vehicle.Vehicle;

import java.util.List;

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
    public User findByTag(Tag tag);

    public List<User> listUsers();

    public User createUser(User user);

    public User getUserById(Long id);

    public Vehicle getVehicleByTag(Tag tag);
}
