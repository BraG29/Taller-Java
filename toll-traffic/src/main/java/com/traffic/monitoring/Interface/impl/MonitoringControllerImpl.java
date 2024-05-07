package com.traffic.monitoring.Interface.impl;

import com.traffic.monitoring.Interface.MonitoringController;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MonitoringControllerImpl implements MonitoringController {

    @Override
    public void notifyVehiclePass() {

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
