package com.traffic.payment.Interface;

import com.traffic.dtos.account.CreditCardDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.dtos.vehicle.VehicleDTO;
import com.traffic.events.NewUserEvent;
import com.traffic.exceptions.InternalErrorException;
import com.traffic.exceptions.InvalidVehicleException;
import com.traffic.exceptions.NoCustomerException;
import com.traffic.exceptions.ExternalApiException;
import jakarta.enterprise.event.Observes;

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
     *
     * @param userEvent evento con la información del usuario que se dió de alta en otro modulo
     * @throws ExternalApiException si hubo un error lanzado por la API de la tarjeta
     * @throws NoCustomerException  si el usuario no es cliente, es decir: <code>user.tollCustomer == null</code>
     */
    public void customerRegistration(@Observes NewUserEvent userEvent)  throws ExternalApiException, NoCustomerException, InternalErrorException;


    /**
     * Notifica a una API de pagos el pago en cuestion
     * if notifyPayment fails, it needs to send the message back to the Payment Queue
     * @param user cliente que realiza el pago
     * @param vehicle vehiculo al que se le habilitara el paso
     * @param amount importe de pasada
     * @param creditCard tarjeta asociada a la cuenta del cliente
     * @throws ExternalApiException si hubo un error lanzado por la API de la tarjeta
     * @throws NoCustomerException si el usuario no es cliente, es decir: <code>user.tollCustomer == null</code>
     * @throws IllegalArgumentException si tanto el usuario y/o el vehiculo son invalidos
     * @throws Exception si la tarjeta de credito es null
     *
     */
    public void notifyPayment(UserDTO user,
                              VehicleDTO vehicle,
                              Double amount,
                              CreditCardDTO creditCard)
            throws ExternalApiException, NoCustomerException, IllegalArgumentException, InvalidVehicleException, Exception;

    /**
     * Recupera todos los pagos hechos por cuenta (PostPaga y Prepaga) en un rango de dias
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
