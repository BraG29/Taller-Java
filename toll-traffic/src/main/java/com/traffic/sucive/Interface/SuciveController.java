package com.traffic.sucive.Interface;

import com.traffic.dtos.vehicle.LicensePlateDTO;
import com.traffic.exceptions.ExternalApiException;
import com.traffic.exceptions.InvalidVehicleException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * Controlador que expone las operaciones del modulo Sucive.
 * </p>
 * Se encarga de gestionar los pagos hechos por Suvive, lo que involucra
 * tanto la recuperacion de datos sobre dichos pagos como la notificacion
 * de los pagos a la API externa
 */
public interface SuciveController {

    /**
     * Notofica el pago al sistema externo
     * @param licensePlate matricula del vehiculo que hizo el pago
     * @param amount cantidad a pagar
     * @throws ExternalApiException si hubo un arror lanzado por la API Sucive
     * @throws IllegalArgumentException
     * @throws InvalidVehicleException si el vehiculo no fue vinculado por un cliente Sucive
     */
    public void notifyPayment(LicensePlateDTO licensePlate,
                              Double amount)
            throws ExternalApiException, IllegalArgumentException, InvalidVehicleException;

    /**
     * Recupera todos los pagos hechos por Sucive en un rango de dias
     * @param from fecha de inicio
     * @param to fecha de fin
     * @return devolvera una <code>List</code> de pagos dentro de un <code>Optional</code>
     */
    public Optional<List<Double>> paymentInquiry(LocalDate from, LocalDate to);

    /**
     * Recupera todos los pagos hechos por Sucive de un vehiculo
     * @param licensePlate matricula del vehiculo
     * @return devolvera una <code>List</code> de pagos dentro de un <code>Optional</code>
     * @throws InvalidVehicleException si el vehiculo no fue vinculado por un cliente Sucive
     */
    public Optional<List<Double>> paymentInquiry(LicensePlateDTO licensePlate) throws InvalidVehicleException;

}
