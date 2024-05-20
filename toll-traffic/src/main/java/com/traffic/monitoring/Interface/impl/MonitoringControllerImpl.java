package com.traffic.monitoring.Interface.impl;

import com.traffic.events.*;
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
    }

    @Override
    public void notifySucivePayment(@Observes SucivePaymentEvent sucivePaymentEvent) {
        System.out.println(sucivePaymentEvent.getDescription());
    }

    @Override
    public void notifyCardPayment(@Observes CreditCardPaymentEvent creditCardPaymentEvent) {
        System.out.println(creditCardPaymentEvent.getDescription());

    }

    @Override
    public void notifyCreditCardPaymentRejected(@Observes CreditCardRejectedEvent creditCardRejectedEvent) {
        System.out.println(creditCardRejectedEvent.getDescription());

    }

    @Override
    public void notifyNotEnoughBalance(@Observes NotEnoughBalanceEvent notEnoughBalanceEvent) {
        System.out.println(notEnoughBalanceEvent.getDescription());
    }
}
