package com.traffic.client.Interface;

import com.traffic.dtos.account.AccountDTO;
import com.traffic.dtos.account.CreditCardDTO;
import com.traffic.dtos.user.TollCustomerDTO;
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
 *     Controlador que expone los metodos del modulo de Gestión de clientes.
 * </p>
 *
 *  Este módulo se encarga de gestionar, la mayoria de las operaciones de los clientes,
 *  vinculaciones de vehiculos y/o tarjetas,
 *  consultas del cliente (saldo, pasadas, vehiculos),
 *  Carga de saldo y realizar pagos.
 *
 */
public interface ClientController {

    /**
     * Registra a un usuario como cliente Telepeaje
     * @param user -> Recibe por parametro  un objeto usuario a agregar
     * @throws NoUserException -> Si el usuario recibido no existe.
     */
    public void addTollCostumer(UserDTO user) throws NoUserException;

    /**
     *  Vincula un vehículo a un cliente del Telepeaje.
     * @param tollCustomer -> Recibe un objeto cliente Telepeaje
     * @param vehicle -> Recibe el objeto vehiculo a vincular con el cliente.
     * @throws NoCustomerException -> Si no existe el cliente.
     * @throws InvalidVehicleException -> Si el vehiculo no existe o no es valido.
     */
    public void linkVehicle(TollCustomerDTO tollCustomer, VehicleDTO vehicle) throws NoCustomerException, InvalidVehicleException; //NocustomerException, InvalidVehicle

    /**
     * Desvincula un vehículo de un cliente del Telepeaje
     * @param tollCustomer -> Recibe un objeto cliente Telepaje
     * @param vehicle -> Recibe un objeto vehiculo a desvincular del cliente
     * @throws NoCustomerException -> Si no existe el cliente.
     * @throws InvalidVehicleException -> Si el vehiculo no existe o no es valido.
     */
    public void unLinkVehicle(TollCustomerDTO tollCustomer, VehicleDTO vehicle) throws  NoCustomerException, InvalidVehicleException;

    /**
     * Devuelve los vehículos vinculados a un cliente
     * @param tollCustomer -> Recibe un objeto cliente Telepeaje
     * @return -> Devuelve una lista con los vehiculos vinculados al cliente.
     * @throws NoCustomerException -> Si no existe el cliente.
     */
    public Optional<List<VehicleDTO>> showLinkedVehicles(TollCustomerDTO tollCustomer) throws NoCustomerException;

    /**
     * Carga Saldo a una cuenta PRE paga de un cliente
     * @param tollCustomer -> Recibe un objeto cliente a cargar saldo.
     * @param balance -> Recibe el saldo (Double) a cargar para dicho cliente
     */
    public void loadBalance(TollCustomerDTO tollCustomer, Double balance) throws NoCustomerException, IllegalArgumentException;

    /**
     * Devuelve el saldo de una cuenta PRE paga de un cliente
     * @param tollCustomer -> Recibe un objeto cliente telepeaje a consultar saldo.
     * @return -> Devuelve el un tipo Double con el saldo del cliente.
     * @throws -> Si no existe el cliente.
     */
    public Optional<Double> showBalance(TollCustomerDTO tollCustomer) throws NoCustomerException;

    /**
     * Asocia una tarjeta de crédito a la cuenta POST paga de un cliente.
     * @param tollCustomerDTO -> Recibe un objeto cliente telepeaje.
     * @param creditCard -> Recibe un objeto tarjeta de credito.
     * @throws NoCustomerException -> Si el cliente recibido no existe.
     */
    public void linkCreditCard(TollCustomerDTO tollCustomerDTO, CreditCardDTO creditCard) throws NoCustomerException;

    /**
     * Devuelve las pasadas realizadas por todos los vehículos registrados por un cliente,en un rango de fechas.
     * @param tollCustomer -> Recibe un objeto cliente telepeaje.
     * @param dateStart -> Recibe una fecha de comienzo, para el rango de fechas.
     * @param endDate -> Recibe una fecha de fin, para el rango de pruebas.
     * @return -> Devuelve una lista de Pasadas de todos los vehiculos del cliente.
     * @throws NoCustomerException -> Si no existe el cliente.
     * @throws IllegalRangeException -> Si el rango de las fechas es invalido.
     */
    public Optional<List<TollPassDTO>> showPastPassages(TollCustomerDTO tollCustomer,
                                                        LocalDate dateStart,
                                                        LocalDate endDate)
                                                       throws NoCustomerException, IllegalRangeException;

    /**
     * Devuelve las pasadas realizadas por un vehículo en particular registrado por un cliente en un rango de fechas.
     * @param tollCustomer -> Recibe un objeto cliente telepeaje.
     * @param vehicle -> Recibe el vehículo objetivo de las pasadas.
     * @param dateStart -> Recibe una fecha de comienzo, para el rango de fechas.
     * @param endDate -> Recibe una fecha de fin, para el rango de pruebas.
     * @return -> Devuelve una lista de Pasadas del vehículo del cliente.
     * @throws NoCustomerException -> Si no existe el cliente.
     * @throws IllegalRangeException -> Si el rango de las fechas es invalido.
     */
    public Optional<List<TollPassDTO>> showPastPassagesVehicle(TollCustomerDTO tollCustomer,
                                                               VehicleDTO vehicle,
                                                               LocalDate dateStart,
                                                               LocalDate endDate) throws NoCustomerException, IllegalRangeException;


    /**
     * Devuelve los tipos de cuentas asociadas al cliente telepeaje,
     * si la cuenta es de PRE pago devuelve, el saldo actual.
     * @param tag -> Recibe un objeto Tag
     * @return -> Devuelve una lista de cuentas asociadas al tag.
     * @throws NoTagException -> Si no existe el tag.
     */
    public Optional<List<AccountDTO>> getAccountByTag(TagDTO tag) throws NoTagException;

    /**
     * Descuenta el importe del pago al saldo del cliente, de su cuenta PRE paga.
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
