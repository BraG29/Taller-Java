package com.traffic.payment.Interface.impl;

import com.traffic.dtos.account.CreditCardDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.dtos.vehicle.VehicleDTO;
import com.traffic.exceptions.ExternalApiException;
import com.traffic.exceptions.InvalidVehicleException;
import com.traffic.exceptions.NoCustomerException;
import com.traffic.payment.Interface.PaymentController;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PaymentControllerImpl implements PaymentController {
    @Override
    public void customerRegistration(UserDTO user,
                                     CreditCardDTO creditCard) throws ExternalApiException, NoCustomerException {

    }

    @Override
    public void notifyPayment(UserDTO user,
                              VehicleDTO vehicle,
                              Double amount,
                              CreditCardDTO creditCard) throws ExternalApiException, NoCustomerException, IllegalArgumentException {

    }

    @Override
    public Optional<List<Double>> paymentInquiry(LocalDate from, LocalDate to) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Double>> paymentInquiry(UserDTO user) throws NoCustomerException {
        return Optional.empty();
    }

    @Override
    public Optional<List<Double>> paymentInquiry(UserDTO user,
                                                 VehicleDTO vehicle) throws NoCustomerException, InvalidVehicleException {
        return Optional.empty();
    }
}
