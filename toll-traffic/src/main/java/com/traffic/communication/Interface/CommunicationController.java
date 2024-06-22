package com.traffic.communication.Interface;

import com.traffic.dtos.user.NotificationDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.events.CreditCardRejectedEvent;
import com.traffic.events.NewUserEvent;
import com.traffic.events.NorifyAllEvent;
import com.traffic.events.NotEnoughBalanceEvent;
import com.traffic.exceptions.NoCustomerException;
import jakarta.enterprise.event.Observes;

import java.util.Optional;
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
     * @param text -> Recibe un texto tipo String con informacíon.
     */
    public void notifyInformation(@Observes NorifyAllEvent e);

    /**
     * Da de alta un cliente
     * @param user -> recibe un objeto tipo cliente telepeaje para dar de alta.
     * @throws NoCustomerException -> Si el usuario recibido no es cliente.
     */
    public void notifyNewCustomer(@Observes NewUserEvent e) throws NoCustomerException;

    /**
     * Devuelve las notificaciones de un cliente en particular
     * @param userId -> Id del usuario
     * @return -> Devuelve un <code>Optional</code> con una lista de notifiaciones del cliente recibido.
     */
    public Optional<List<NotificationDTO>> getNotificationByCostumer(Long userId);

}
