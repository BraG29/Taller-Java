package com.traffic.monitoring.Interface.impl;

import com.traffic.events.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@ApplicationScoped
@Path("/monitoring")
public class MonitorTestAPI {

    @Inject
    private Event<CustomEvent> singleEvent;
    @GET
    @Path("/test")
    public String FireTest() {

            CustomEvent customEvent = new VehiclePassEvent("Descripcion de prueba para la pasada de un vehiculo");
            singleEvent.fire(customEvent);

            CustomEvent customEvent2 = new SucivePaymentEvent("Descripcion de prueba para pago con sucive");
            singleEvent.fire(customEvent2);

            CustomEvent customEvent3 = new CreditCardPaymentEvent("Descripcion de prueba para pago con tarjeta");
            singleEvent.fire(customEvent3);

            CustomEvent customEvent4 = new CreditCardRejectedEvent("Descripcion de prueba para pago con tarjeta rechazada");
            singleEvent.fire(customEvent4);

            CustomEvent customEvent5 = new NotEnoughBalanceEvent("Descripcion de prueba para evento de saldo insuficiente");
            singleEvent.fire(customEvent5);

        return "chupachichi";
    }
}
