package com.traffic.communication.Interface.impl;

import com.traffic.communication.Interface.CommunicationController;
import com.traffic.dtos.user.NotificationDTO;
import com.traffic.dtos.user.TollCustomerDTO;
import com.traffic.exceptions.NoCustomerException;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CommunicationControllerImpl implements CommunicationController {

    @Override
    public void notifyNotEnoughBalance(TollCustomerDTO tollCustomer) throws NoCustomerException {

    }

    @Override
    public void notifyBlockedCreditCard(TollCustomerDTO tollCustomer) throws NoCustomerException {

    }

    @Override
    public void notifyInformation(String text) {

    }

    @Override
    public void addCostumer(TollCustomerDTO tollCustomer) throws NoCustomerException {

    }

    @Override
    public Optional<List<NotificationDTO>> getNotificationByCostumer(TollCustomerDTO tollCustomer) throws NoCustomerException {
        return Optional.empty();
    }
}
