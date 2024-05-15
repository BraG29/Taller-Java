package com.traffic.monitoring.Interface.impl;

import com.traffic.events.*;
import com.traffic.monitoring.Interface.MonitoringController;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

@EnableAutoWeld
@AddPackages(MonitoringControllerImpl.class)
class MonitoringControllerTest {

    // Monitoring Controller Test v0.01, for now a the monitoring module is a simple observer

    @Inject
    MonitoringController monitoringController;
    @Inject
    private Event<CustomEvent> singleEvent;

    @Test
    void vehiclePassTest(){
        CustomEvent customEvent = new VehiclePassEvent("Descripcion de prueba para la pasada de un vehiculo");
        singleEvent.fire(customEvent);
    }

    @Test
    void sucivePaymentTest(){
        CustomEvent customEvent = new SucivePaymentEvent("Descripcion de prueba para pago con sucive");
        singleEvent.fire(customEvent);
    }

    @Test
    void cardPaymentTest(){
        CustomEvent customEvent = new CreditCardPaymentEvent("Descripcion de prueba para pago con tarjeta");
        singleEvent.fire(customEvent);
    }

    @Test
    void cardPaymentRejectedTest(){
        CustomEvent customEvent = new CreditCardRejectedEvent("Descripcion de prueba para pago con tarjeta rechazada");
        singleEvent.fire(customEvent);
    }
    @Test
    void notEnoughBalanceTest(){
        CustomEvent customEvent = new NotEnoughBalanceEvent("Descripcion de prueba para evento de saldo insuficiente");
        singleEvent.fire(customEvent);
    }

}