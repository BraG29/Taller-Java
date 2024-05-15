package com.traffic.communication.Interface.impl;

import com.traffic.communication.Interface.CommunicationController;
import com.traffic.communication.Interface.NotificationRepository;
import com.traffic.communication.domain.entities.Notification;
import com.traffic.dtos.user.NotificationDTO;
import com.traffic.dtos.user.UserDTO;
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

    private void addNotification(Long userId, Notification notification){
        if( userId == 0L ){
            notificationRepository.addNotification(notification);

        }else{
            notificationRepository.addNotification(userId, notification);
        }
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
    public void notifyNotEnoughBalance(UserDTO user) throws NoCustomerException {

        if(user.getTollCustomer() == null){
            throw new NoCustomerException();
        }

        String content = "No se ha permitido la pasada por peaje por falta de fondos en su cuenta Pre Paga.";

        printOnConsole(user, content);

        addNotification(user.getId(),
                new Notification(LocalDate.now(), content));
    }

    @Override
    public void notifyBlockedCreditCard(UserDTO user) throws NoCustomerException {
        if(user.getTollCustomer() == null){
            throw new NoCustomerException();
        }

        String content = "No se ha podido concretar el pago porque su tarjeta ha sido bloqueada";

        printOnConsole(user, content);

        addNotification(user.getId(),
                new Notification(LocalDate.now(), content));
    }

    @Override
    public void notifyInformation(String text) {
        System.out.println(String.format("""
                Notificando a todos los Usuarios:
                %s
                """, text));

        addNotification(0L,
                new Notification(LocalDate.now(), text));
    }

    @Override
    public void addCostumer(UserDTO user) throws NoCustomerException {

        if(user.getTollCustomer() == null){
            throw new NoCustomerException();
        }

        String content = "Usted se ha dado de alta como cliente del peaje";

        printOnConsole(user, content);

        addNotification(user.getId(),
                new Notification(LocalDate.now(), content));

    }

    @Override
    public Optional<List<NotificationDTO>> getNotificationByCostumer(UserDTO user) throws NoCustomerException {

        if(user.getTollCustomer() == null){
            throw new NoCustomerException();
        }

        Optional<List<NotificationDTO>> out = Optional.empty();

        Optional<List<Notification>> notificationsOPT = notificationRepository.findByUser(user.getId());

        if(notificationsOPT.isPresent()){

            final List<NotificationDTO> notificationDTOS = new ArrayList<NotificationDTO>();

            notificationsOPT.get().forEach(notification -> {
                notificationDTOS.add(
                        new NotificationDTO(notification.getDate(),
                                notification.getMessage()));
            });

            out = Optional.of(notificationDTOS);
        }

        return out;
    }
}
