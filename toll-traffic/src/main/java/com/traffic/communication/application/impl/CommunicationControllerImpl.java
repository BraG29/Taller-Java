package com.traffic.communication.application.impl;

import com.traffic.communication.Interface.CommunicationController;
import com.traffic.communication.domain.repository.NotificationRepository;
import com.traffic.communication.domain.entities.Notification;
import com.traffic.dtos.user.NotificationDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.events.CreditCardRejectedEvent;
import com.traffic.events.NewUserEvent;
import com.traffic.events.NorifyAllEvent;
import com.traffic.events.NotEnoughBalanceEvent;
import com.traffic.exceptions.NoCustomerException;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CommunicationControllerImpl implements CommunicationController {

    private String emailFormat;
    @Inject
    private NotificationRepository notificationRepository;

    @PostConstruct
    public void initEmailFormat(){
        emailFormat = """
                Correo enviado a Usuario con C.I.: %s
                Nombre: %s
                Correo: %s
                Contenido:
                %s
                """;
    }

    private void printOnConsole(UserDTO user, String content){
        System.out.println(String
                .format(emailFormat,
                        user.getCi(),
                        user.getName(),
                        user.getEmail(),
                        content));

    }

    @Override
    public void notifyNotEnoughBalance(NotEnoughBalanceEvent e) {

    }

    @Override
    public void notifyBlockedCreditCard(CreditCardRejectedEvent e) {

    }

    @Override
    public void notifyInformation(NorifyAllEvent e) {

    }

    @Override
    public void notifyNewCustomer(NewUserEvent e) throws NoCustomerException {

    }

    @Override
    public Optional<List<NotificationDTO>> getNotificationByCostumer(Long userId) {
        return Optional.empty();
    }
}
