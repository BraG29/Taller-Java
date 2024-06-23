package com.traffic.communication.application.impl;

import com.traffic.communication.Interface.CommunicationController;
import com.traffic.communication.domain.entities.User;
import com.traffic.communication.domain.repository.NotificationRepository;
import com.traffic.communication.domain.entities.Notification;
import com.traffic.communication.domain.repository.UserRepository;
import com.traffic.dtos.user.NationalUserDTO;
import com.traffic.dtos.user.NotificationDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.events.CreditCardRejectedEvent;
import com.traffic.events.NewUserEvent;
import com.traffic.events.NorifyAllEvent;
import com.traffic.events.NotEnoughBalanceEvent;
import com.traffic.exceptions.NoCustomerException;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Local;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CommunicationControllerImpl implements CommunicationController {

    private String emailFormat;
    @Inject
    private NotificationRepository notificationRepository;
    @Inject
    private UserRepository userRepository;

    @PostConstruct
    public void initEmailFormat() {
        emailFormat = """
                Correo enviado a Usuario con C.I.: %s
                Nombre: %s
                Correo: %s
                Contenido:
                %s
                """;
    }

    private void printOnConsole(UserDTO user, String content) {
        System.out.printf((emailFormat) + "%n",
                user.getCi(),
                user.getName(),
                user.getEmail(),
                content);
    }

    @Transactional
    private void notifyUser(Long userId, Notification notification) {

        userRepository.findById(userId).ifPresent(u -> {
            notificationRepository.save(notification).ifPresent(n -> {
                u.getNotifications().add(n);

                userRepository.save(u).ifPresent(usr -> {

                    printOnConsole(new NationalUserDTO(
                                    null,
                                    usr.getEmail(),
                                    null,
                                    usr.getName(),
                                    usr.getCi(),
                                    null,
                                    null,
                                    null,
                                    null),
                            n.getMessage());
                });
            });
        });
    }

    @Override
    public void notifyNotEnoughBalance(NotEnoughBalanceEvent e) {
        String content = "Se le ha denegado la pasada por peaje por falta de fondos en su cuenta Pre Paga";

        notifyUser(
                e.getUserId(),
                new Notification(null, LocalDate.now(), content));
    }

    @Override
    public void notifyBlockedCreditCard(CreditCardRejectedEvent e) {
        String content = "No se la ha podido cobrar a su tarjeta de credito ya que fue rechazada";

        notifyUser(
                e.getUserId(),
                new Notification(null, LocalDate.now(), content));
    }

    @Override
    @Transactional
    public void notifyInformation(NorifyAllEvent e) {
        List<User> users = userRepository.findAll();

        if(!users.isEmpty()) {
            notificationRepository
                    .save(new Notification(null, LocalDate.now(), e.getDescription()))
                    .ifPresent( notification -> {
                        users.forEach(user -> {
                            user.getNotifications().add(notification);
                            userRepository.save(user);
                        });
                    });
        }
    }

    @Override
    public void notifyNewCustomer(NewUserEvent e) {
        String content = "Usted se ha dado de alta en el sistema";

        notifyUser(
                e.getUser().getId(),
                new Notification(null, LocalDate.now(), content));
    }

    @Override
    public List<NotificationDTO> getNotificationByCostumer(Long userId) {
        List<NotificationDTO> notifications = new ArrayList<NotificationDTO>();

        userRepository.findById(userId).ifPresent( user -> {
            for(Notification n : user.getNotifications()) {
                notifications.add(new NotificationDTO(
                        n.getId(),
                        n.getDate(),
                        n.getMessage()));
            }
        });

        return notifications;
    }
}
