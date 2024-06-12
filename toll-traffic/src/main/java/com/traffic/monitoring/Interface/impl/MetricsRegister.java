package com.traffic.monitoring.Interface.impl;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.influx.InfluxConfig;
import io.micrometer.influx.InfluxMeterRegistry;


@ApplicationScoped
public class MetricsRegister {
    public static final String VEHICLE_PASS_COUNTER ="notifyVehiclePass";
    public static final String SUCIVE_PAYMENT_COUNTER ="notifySucivePayment";
    public static final String PRE_PAYMENT_COUNTER ="notifyPrePayment";
    public static final String POST_PAYMENT_COUNTER ="notifyPostPayment";

    //Maybe for future monitoring
    //public static final String CARD_PAYMENT_REJECTED_COUNTER ="notifyCreditCardPaymentRejected";
    //public static final String NOT_ENOUGH_BALANCE_COUNTER ="notifyNotEnoughBalance";



    private InfluxConfig config;

    @PostConstruct
    public void init() {
        //configuración del repositorio de metricas (influxdb)
        config = new InfluxConfig() {
            @Override
            public String get(String s) {
                return null;
            }

            @Override
            public Duration step() {
                return Duration.ofSeconds(1);
            }

            @Override
            public String db() {
                return "tollTrafficMetrics";
            }
        };

    }

    public void incrementCounter(String nombreCounter) {
        MeterRegistry meterRegistry;
        meterRegistry = new InfluxMeterRegistry(config, Clock.SYSTEM);
        meterRegistry.counter(nombreCounter).increment();
    }
}
