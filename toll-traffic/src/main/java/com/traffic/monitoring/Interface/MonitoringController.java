package com.traffic.monitoring.Interface;

import com.traffic.events.*;
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
    public void notifyVehiclePass(@Observes VehiclePassEvent vehiclePassEvent); //Toll Module

    /**
     * Envía un evento que representa el cobro con sucive
     */
    public void notifySucivePayment(@Observes SucivePaymentEvent sucivePaymentEvent); //Sucive Module

    /**
     * Envía un evento que representa el cobro con Saldo
     */
    public void notifyPrePayment(@Observes PREPayTollPassEvent prePayTollPassEvent); //Payment Module

    /**
     * Envía un evento que representa el cobro con tarjeta
     */
    public void notifyPostPayment(@Observes CreditCardPaymentEvent creditCardPaymentEvent); //Payment Module


    //Maybe for future Monitoring

    /**
     * Envía un evento que representa el rechazo del cobro por tarjeta.
     */
//    public void notifyCreditCardPaymentRejected(@Observes CreditCardRejectedEvent creditCardRejectedEvent); //Payment Module

    /**
     * Envía un evento que representa el rechazo de cobro por saldo insuficiente.
     */
//    public void notifyNotEnoughBalance(@Observes NotEnoughBalanceEvent notEnoughBalanceEvent); //Toll Module

}
