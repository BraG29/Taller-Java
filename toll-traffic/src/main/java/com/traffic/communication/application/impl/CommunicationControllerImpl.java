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
import com.traffic.events.NotifyAllEvent;
import com.traffic.events.NotEnoughBalanceEvent;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

//    @Transactional
    private void notifyUser(Long userId, Notification notification) {

        System.out.println("Entrando a notifyUser()--------------------------:::::::::::::::::::::::::::::>>>>>>>>>>>>>>>>>>>>");

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
    public void notifyNotEnoughBalance(@Observes NotEnoughBalanceEvent e) {
        String content = "Se le ha denegado la pasada por peaje por falta de fondos en su cuenta Pre Paga";

        notifyUser(
                e.getUserId(),
                new Notification(null, LocalDate.now(), content));
    }

    @Override
    public void notifyBlockedCreditCard(@Observes CreditCardRejectedEvent e) {
        String content = "No se la ha podido cobrar a su tarjeta de credito ya que fue rechazada";

        notifyUser(
                e.getUserId(),
                new Notification(null, LocalDate.now(), content));
    }

    @Override
//    @Transactional
    public void notifyInformation(@Observes NotifyAllEvent e) {
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
    public void notifyNewCustomer(@Observes NewUserEvent e) {

        String content = "Usted se ha dado de alta en el sistema";

        List<Notification> notifications = new ArrayList<Notification>();
        notificationRepository
                .save(new Notification(null, LocalDate.now(), content))
                .ifPresent(notifications::add);

        UserDTO userDTO = e.getUser();
        User user = new User(userDTO.getId(),
                userDTO.getCi(),
                userDTO.getName(),
                userDTO.getEmail(),
                notifications);

        userRepository.save(user);
        printOnConsole(userDTO, content);
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
