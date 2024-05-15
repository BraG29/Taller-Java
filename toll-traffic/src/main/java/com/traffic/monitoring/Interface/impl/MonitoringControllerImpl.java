package com.traffic.monitoring.Interface.impl;

import com.traffic.events.CustomEvent;
import com.traffic.events.VehiclePassEvent;
import com.traffic.monitoring.Interface.MonitoringController;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class MonitoringControllerImpl implements MonitoringController {


    @Inject
    private Event<CustomEvent> singleEvent;

    @Override
    public void notifyVehiclePass(@Observes VehiclePassEvent vehiclePassEvent) {
        System.out.println(vehiclePassEvent.getDescription());
//        singleEvent.fire(vehiclePassEvent);
    }

    @Override
    public void notifySucivePayment() {

    }

    @Override
    public void notifyCardPayment() {

    }

    @Override
    public void notifyCreditCardPaymentRejected() {

    }

    @Override
    public void notifyNotEnoughBalance() {

    }
}
