package com.traffic.communication.Interface;

import com.traffic.dtos.user.NotificationDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.exceptions.NoCustomerException;

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
     * @param user -> Recibe un objeto cliente telepeaje.
     * @throws NoCustomerException -> Si el usuario recibido no es cliente.
     */
    public void notifyNotEnoughBalance(UserDTO user) throws NoCustomerException;

    /**
     * Noitifica vía emial al Cliente que su tarjete fue bloqueada
     * @param user -> Recibe un objeto cliente telepeaje.
     * @throws NoCustomerException -> Si el usuario recibido no es cliente.
     */
    public void notifyBlockedCreditCard(UserDTO user) throws NoCustomerException;

    /**
     * Notifica vía email al Cliente alguna información relevante.
     * @param text -> Recibe un texto tipo String con informacíon.
     */
    public void notifyInformation(String text);

    /**
     * Da de alta un cliente
     * @param user -> recibe un objeto tipo cliente telepeaje para dar de alta.
     * @throws NoCustomerException -> Si el usuario recibido no es cliente.
     */
    public void addCostumer(UserDTO user) throws NoCustomerException;

    /**
     * Devuelve las notificaciones de un cliente en particular
     * @param user -> Recibe un objeto tipo cliente telepeaje para devolver notificaciones.
     * @return -> Devuelve una lista de notifiaciones del cliente recibido.
     * @throws NoCustomerException -> Si el usuario recibido no es cliente.
     */
    public Optional<List<NotificationDTO>> getNotificationByCostumer(UserDTO user) throws NoCustomerException;

}
