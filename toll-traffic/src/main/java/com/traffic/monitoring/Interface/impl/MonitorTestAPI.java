package com.traffic.monitoring.Interface.impl;

import com.traffic.events.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

        CustomEvent customEvent4 = new PREPayTollPassEvent();

        singleEvent.fire(customEvent4);

        return "chupachichi";
    }

    @Inject
    @GET
    @Path("/Random5minuteTest")
    public String FireRandom5MinuteTest() {
        Random random = new Random();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        long endTime = System.currentTimeMillis() + 5 * 60 * 1000; // 5 minutes

        executor.scheduleAtFixedRate(() -> {
            int eventNumber = random.nextInt(3);
            CustomEvent paymentEvent;

            switch (eventNumber) {
                case 0:
                    paymentEvent = new SucivePaymentEvent("Descripcion de prueba para pago con sucive");
                    break;
                case 1:
                    paymentEvent = new CreditCardPaymentEvent("Descripcion de prueba para pago con tarjeta");
                    break;
                case 2:
                    paymentEvent = new PREPayTollPassEvent();
                    break;
                default:
                    paymentEvent = new CustomEvent("Default event description");
                    break;
            }

            CustomEvent vehiclePassEvent = new VehiclePassEvent("Descripcion de prueba para la pasada de un vehiculo");
            singleEvent.fire(vehiclePassEvent);
            singleEvent.fire(paymentEvent);

            if (System.currentTimeMillis() >= endTime) {
                executor.shutdown();
            }
        }, random.nextInt(1) + 1, random.nextInt(3) + 1, TimeUnit.SECONDS);

        return "Random 5-minute test scheduled with random intervals between 300 and 1000 MILLISECONDS";
    }
}



