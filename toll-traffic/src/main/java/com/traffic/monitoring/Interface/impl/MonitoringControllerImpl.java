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
    private MetricsRegister register;

    @Inject
    private Event<CustomEvent> singleEvent;

    @Override
    public void notifyVehiclePass(@Observes VehiclePassEvent vehiclePassEvent) {
        System.out.println(vehiclePassEvent.getDescription());
        register.incrementCounter(register.VEHICLE_PASS_COUNTER);
    }

    @Override
    public void notifySucivePayment(@Observes SucivePaymentEvent sucivePaymentEvent) {
        System.out.println(sucivePaymentEvent.getDescription());
        register.incrementCounter(register.SUCIVE_PAYMENT_COUNTER);
    }

    @Override
    public void notifyPrePayment(@Observes PREPayTollPassEvent prePayTollPassEvent) {
        System.out.println(prePayTollPassEvent.getDescription());
        register.incrementCounter(register.PRE_PAYMENT_COUNTER);
    }

    @Override
    public void notifyPostPayment(@Observes CreditCardPaymentEvent postPayTollPassEvent) {
        System.out.println(postPayTollPassEvent.getDescription());
        register.incrementCounter(register.POST_PAYMENT_COUNTER);
    }

//    Maybe for future Monitoring

//    @Override
//    public void notifyCreditCardPaymentRejected(@Observes CreditCardRejectedEvent creditCardRejectedEvent) {
//        System.out.println(creditCardRejectedEvent.getDescription());
//        register.incrementCounter(register.CARD_PAYMENT_REJECTED_COUNTER);
//    }
//
//    @Override
//    public void notifyNotEnoughBalance(@Observes NotEnoughBalanceEvent notEnoughBalanceEvent) {
//        System.out.println(notEnoughBalanceEvent.getDescription());
//        register.incrementCounter(register.NOT_ENOUGH_BALANCE_COUNTER);
//    }


}
