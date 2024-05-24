package com.traffic.sucive.Interface.impl;

import com.traffic.dtos.vehicle.LicensePlateDTO;
import com.traffic.sucive.Interface.SuciveController;
import com.traffic.sucive.domain.repository.SuciveRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SuciveControllerImpl implements SuciveController {

    @Inject
    private SuciveRepository repository;

    @Override
    public void notifyPayment(LicensePlateDTO licensePlate, Double amount) {

    }

    @Override
    public Optional<List<Double>> paymentInquiry(LocalDate from, LocalDate to) {

        return Optional.empty();
    }

    @Override
    public Optional<List<Double>> paymentInquiry(LicensePlateDTO licensePlate) {
        return Optional.empty();
    }
}
