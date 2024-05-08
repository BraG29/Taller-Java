package com.traffic.client.Interface;

import com.traffic.dtos.account.AccountDTO;
import com.traffic.dtos.account.CreditCardDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.dtos.vehicle.TagDTO;
import com.traffic.dtos.vehicle.TollPassDTO;
import com.traffic.dtos.vehicle.VehicleDTO;
import com.traffic.exceptions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 *
 * <p>
 *     Controlador que expone los metodos del modulo de Gestión de usuarios.
 * </p>
 *
 *  Este módulo se encarga de gestionar, la mayoria de las operaciones de los usuarios,
 *  vinculaciones de vehiculos y/o tarjetas,
 *  consultas del usuario (saldo, pasadas, vehiculos),
 *  Carga de saldo y realizar pagos.
 *
 */
public interface ClientController {

    /**
     * Registra a un usuario como usuario
     * @param user -> Recibe por parametro  un objeto usuario a agregar
     * @throws IllegalArgumentException -> Si el objeto usuario tiene información incorrecta.
     */
    public void addTollCostumer(UserDTO user) throws IllegalArgumentException;

    /**
     *  Vincula un vehículo a un usuario.
     * @param user -> Recibe un objeto usuario
     * @param vehicle -> Recibe el objeto vehiculo a vincular con el usuario.
     * @throws IllegalArgumentException -> Si un objeto usuario o vehiculo tiene información incorrecta.
     */
    public void linkVehicle(UserDTO user,
                            VehicleDTO vehicle) throws IllegalArgumentException;

    /**
     * Desvincula un vehículo de un usuario
     * @param user -> Recibe un objeto usuario Telepaje
     * @param vehicle -> Recibe un objeto vehiculo a desvincular del usuario
     * @throws IllegalArgumentException -> Si un objeto usuario tiene información incorrecta.
     * @throws InvalidVehicleException -> Si el vehiculo no esta vinculado al usuario.
     */
    public void unLinkVehicle(UserDTO user,
                              VehicleDTO vehicle) throws  IllegalArgumentException, InvalidVehicleException;

    /**
     * Devuelve los vehículos vinculados a un usuario
     * @param user -> Recibe un objeto usuario
     * @return -> Devuelve una lista con los vehiculos vinculados al usuario.
     * @throws IllegalArgumentException -> Si el usuario tiene información incorrecta.
     */
    public Optional<List<VehicleDTO>> showLinkedVehicles(UserDTO user) throws IllegalArgumentException;

    /**
     * Carga Saldo a una cuenta PRE paga de un usuario
     * @param user -> Recibe un objeto usuario a cargar saldo.
     * @param balance -> Recibe el saldo (Double) a cargar para dicho usuario
     * @throws IllegalArgumentException -> Si un objeto usuario tiene información incorrecta.
     */
    public void loadBalance(UserDTO user,
                            Double balance) throws IllegalArgumentException, IllegalArgumentException;

    /**
     * Devuelve el saldo de una cuenta PRE paga de un usuario
     * @param user -> Recibe un objeto usuario a consultar saldo.
     * @return -> Devuelve el un tipo Double con el saldo del usuario.
     * @throws IllegalArgumentException -> Si no existe el usuario.
     */
    public Optional<Double> showBalance(UserDTO user) throws IllegalArgumentException;

    /**
     * Asocia una tarjeta de crédito a la cuenta POST paga de un usuario.
     * @param UserDTO -> Recibe un objeto usuario.
     * @param creditCard -> Recibe un objeto tarjeta de credito.
     * @throws IllegalArgumentException -> Si el objeto usuario tiene información incorrecta.
     */
    public void linkCreditCard(UserDTO UserDTO, CreditCardDTO creditCard) throws IllegalArgumentException;

    /**
     * Devuelve las pasadas realizadas por todos los vehículos registrados por un usuario,en un rango de fechas.
     * @param user -> Recibe un objeto usuario.
     * @param from -> Recibe una fecha de comienzo, para el rango de fechas.
     * @param to -> Recibe una fecha de fin, para el rango de pruebas.
     * @return -> Devuelve una lista de Pasadas de todos los vehiculos del usuario.
     * @throws IllegalArgumentException -> Si el objeto usuario tiene información incorrecta.
     * @throws IllegalRangeException -> Si el rango de las fechas es invalido, cuando la diferencia de dias entre fechas es menor a 0.
     */
    public Optional<List<TollPassDTO>> showPastPassages(UserDTO user,
                                                        LocalDate from,
                                                        LocalDate to)
                                                       throws IllegalArgumentException, IllegalRangeException;

    /**
     * Devuelve las pasadas realizadas por un vehículo en particular registrado por un usuario en un rango de fechas.
     * @param user -> Recibe un objeto usuario.
     * @param vehicle -> Recibe el vehículo objetivo de las pasadas.
     * @param from -> Recibe una fecha de comienzo, para el rango de fechas.
     * @param to -> Recibe una fecha de fin, para el rango de pruebas.
     * @return -> Devuelve una lista de Pasadas del vehículo del usuario.
     * @throws IllegalArgumentException -> Si no existe el usuario.
     * @throws IllegalRangeException -> Si el rango de las fechas es invalido, cuando la diferencia de dias entre fechas es menor a 0.
     */
    public Optional<List<TollPassDTO>> showPastPassagesVehicle(UserDTO user,
                                                               VehicleDTO vehicle,
                                                               LocalDate from,
                                                               LocalDate to) throws IllegalArgumentException, IllegalRangeException;


    /**
     * Devuelve los tipos de cuentas asociadas al usuario,
     * si la cuenta es de PRE pago devuelve, el saldo actual.
     * @param tag -> Recibe un objeto Tag
     * @return -> Devuelve una lista de cuentas asociadas al tag.
     * @throws IllegalArgumentException -> Si no existe el tag.
     */
    public Optional<List<AccountDTO>> getAccountByTag(TagDTO tag) throws IllegalArgumentException;

    /**
     * Descuenta el importe del pago al saldo del usuario, de su cuenta PRE paga.
     * @param balance -> Importe de tipo Double a descontar.
     * @throws IllegalArgumentException -> Si el tipo de dato es invalido.
     */
    public void prePay(Double balance) throws IllegalArgumentException;

    /**
     * Realiza un pago utilizando tarjeta de crédito.
     * @param balance -> Importe a cobrar.
     * @throws IllegalArgumentException -> Si el tipo de dato es invalido.
     */
    public void postPay(Double balance) throws IllegalArgumentException;
}
