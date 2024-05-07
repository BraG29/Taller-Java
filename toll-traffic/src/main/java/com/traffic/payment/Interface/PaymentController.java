package com.traffic.payment.Interface;

import com.traffic.dtos.account.CreditCardDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.dtos.vehicle.VehicleDTO;
import com.traffic.exceptions.InvalidVehicleException;
import com.traffic.exceptions.NoCustomerException;
import com.traffic.exceptions.ExternalApiException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 *     Controlador que expone los metodos del modulo Medio de Pagos
 * </p>
 */
public interface PaymentController {

    /**
     * Registra una cuenta para un usuario con una tarjeta
     * @param user usuario que abre la cuenta
     * @param creditCard tarjeta (credito o debito) con la que abre la cuenta
     * @throws ExternalApiException si hubo un error lanzado por la API de la tarjeta
     * @throws NoCustomerException si el usuario no es cliente, es decir: <code>user.tollCustomer == null</code>
     */
    public void customerRegistration(UserDTO user,
                                     CreditCardDTO creditCard) throws ExternalApiException, NoCustomerException;

    /**
     * Notifica a una API de pagos el pago en cuestion
     * @param user cliente que realiza el pago
     * @param vehicle vehiculo al que se le habilitara el paso
     * @param amount importe de pasada
     * @param creditCard tarjeta asociada a la cuenta del cliente
     * @throws ExternalApiException si hubo un error lanzado por la API de la tarjeta
     * @throws NoCustomerException si el usuario no es cliente, es decir: <code>user.tollCustomer == null</code>
     * @throws IllegalArgumentException
     */
    public void notifyPayment(UserDTO user,
                              VehicleDTO vehicle,
                              Double amount,
                              CreditCardDTO creditCard) throws ExternalApiException, NoCustomerException, IllegalArgumentException;

    /**
     * Recupera todos los pagos hechos por cuenta (PostPaga y Pregaga) en un rango de dias
     * @param from fecha de inicio
     * @param to fecha de fin
     * @return devolvera una <code>List</code> de pagos dentro de un <code>Optional</code>
     */
    public Optional<List<Double>> paymentInquiry(LocalDate from, LocalDate to);

    /**
     * Recupera todos los pagos hechos por cuenta (PostPaga y Pregaga) de un cliente
     * @param user cliente que realizo los pagos
     * @return devolvera una <code>List</code> de pagos dentro de un <code>Optional</code>
     * @throws NoCustomerException si el usuario no es cliente, es decir: <code>user.tollCustomer == null</code>
     */
    public Optional<List<Double>> paymentInquiry(UserDTO user) throws NoCustomerException;

    /**
     * Recupera todos los pagos hechos por cuenta (PostPaga y Pregaga) de un cliente con un vehiculo
     * @param user cliente que realizo los pagos
     * @param vehicle vehiculo asociado al cliente
     * @return devolvera una <code>List</code> de pagos dentro de un <code>Optional</code>
     * @throws NoCustomerException si el usuario no es cliente, es decir: <code>user.tollCustomer == null</code>
     * @throws InvalidVehicleException si el vehiculo no esta asociado al cliente
     */
    public Optional<List<Double>> paymentInquiry(UserDTO user, VehicleDTO vehicle) throws NoCustomerException, InvalidVehicleException;


}
