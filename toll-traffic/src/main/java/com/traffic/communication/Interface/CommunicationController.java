package com.traffic.communication.Interface;

import com.traffic.dtos.user.NotificationDTO;
import com.traffic.events.CreditCardRejectedEvent;
import com.traffic.events.NewUserEvent;
import com.traffic.events.NotifyAllEvent;
import com.traffic.events.NotEnoughBalanceEvent;
import jakarta.enterprise.event.Observes;

import java.util.List;

/**
 *
 * <p>
 *     Controlador que expone los metodos del modulo de Comunicación.
 * </p>
 *
 * Este modulo se encarga de notificar al cliente via mail distintos eventos como:
 * falta de fondos, tarjetas rechazadas, entre otros.
 *
 */
public interface CommunicationController {

    /**
     *  Notifica vía email al Cliente que  su saldo de cuenta PRE paga es insuficiente
     */
    public void notifyNotEnoughBalance(@Observes NotEnoughBalanceEvent e);

    /**
     * Noitifica vía emial al Cliente que su tarjete fue bloqueada
     */
    public void notifyBlockedCreditCard(@Observes CreditCardRejectedEvent e);

    /**
     * Notifica vía email al Cliente alguna información relevante.
     */
    public void notifyInformation(@Observes NotifyAllEvent e);

    /**
     * Da de alta un cliente
     */
    public void notifyNewCustomer(@Observes NewUserEvent e);

    /**
     * Devuelve las notificaciones de un cliente en particular
     * @param userId -> Id del usuario
     * @return -> Una lista con los DTO de las Notificaciones. Lista vacia si no hay notifiaciones
     */
    public List<NotificationDTO> getNotificationByCostumer(Long userId);

}
