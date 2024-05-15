package com.traffic.communication.Interface.impl;

import com.traffic.communication.Interface.CommunicationController;
import com.traffic.communication.Interface.NotificationRepository;
import com.traffic.communication.domain.entities.Notification;
import com.traffic.dtos.user.NationalUserDTO;
import com.traffic.dtos.user.TollCustomerDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.exceptions.NoCustomerException;
import jakarta.inject.Inject;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

/**
 * Tests para las operaciones de Communication Controller
 * El codigo que queda sin cubrir se considera ya cubierto por los demas tests
 */
@EnableAutoWeld
@AddPackages(CommunicationControllerImpl.class)
@AddPackages(NotificationRepositoryImpl.class)
class CommunicationControllerTest {

    @Inject
    private CommunicationController communicationController;
    @Inject
    private NotificationRepository notificationRepository;
    private static UserDTO customer;
    private static UserDTO notCustomer;

    @BeforeAll
    static void setUp(){
        customer = new NationalUserDTO(
                1L,
                "pepe@mail.com",
                null,
                "Pepe Garcia",
                "1.111.111-1",
                new TollCustomerDTO(),
                null,
                null,
                null
        );

        notCustomer = new NationalUserDTO(
                1L,
                "pepe@mail.com",
                null,
                "Pepe Garcia",
                "1.111.111-1",
                null,
                null,
                null,
                null);
    }

    @DisplayName("Test de para lanzamiento de errores")
    @Test
    void exceptionsThrowsTest(){
        Assertions.assertThrows(NoCustomerException.class, () -> {
            communicationController.notifyNotEnoughBalance(notCustomer);
        });
    }

    @DisplayName("Test de notificaciones correctamente cargadas")
    @Test
    void successTests() throws NoCustomerException{

        int initialSize = notificationRepository.findByUser(customer.getId()).get().size();
        communicationController.notifyNotEnoughBalance(customer);
        int actualSize = notificationRepository.findByUser(customer.getId()).get().size();

        Assertions.assertEquals(actualSize, initialSize + 1);
    }

    @DisplayName("Test para Notificar a todos y buscar notificaciones por usuario")
    @Test
    void notifyAllFindByUserTest(){
        Notification notification = new Notification(LocalDate.now(), "Mensaje prueba");

        notificationRepository.addNotification(notification);

        Assertions.assertDoesNotThrow(() -> {
            List<Notification> notificationsFor1 = notificationRepository.findByUser(1L).orElseThrow();
            List<Notification> notificationsFor2 = notificationRepository.findByUser(2L).orElseThrow();

            Assertions.assertTrue( notificationsFor1.contains(notification) &&
                    notificationsFor2.contains(notification) );
        });



    }

}