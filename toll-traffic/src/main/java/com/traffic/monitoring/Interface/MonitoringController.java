package com.traffic.monitoring.Interface;

import com.traffic.events.VehiclePassEvent;
import jakarta.enterprise.event.Observes;

/**
 * <p>
 *     Controlador que expone los metodos del modulo de Monitoreo.
 * </p>
 */
public interface MonitoringController {

    /**
     * Envía un evento que representa el pasaje de un vehículo.
     */
    public void notifyVehiclePass(@Observes VehiclePassEvent vehiclePassEvent);

    /**
     * Envía un evento que representa el cobro con sucive
     */
    public void notifySucivePayment();

    /**
     * Envía un evento que representa el cobro con tarjeta
     */
    public void notifyCardPayment();

    /**
     * Envía un evento que representa el rechazo del cobro por tarjeta.
     */
    public void notifyCreditCardPaymentRejected();

    /**
     * Envía un evento que representa el rechazo de cobro por saldo insuficiente.
     */
    public void notifyNotEnoughBalance();

}
