package com.traffic.exceptions;

/**
 * <p>
 *     Excepcion que se lanza cuando:
 *     <ul>
 *         <li>
 *             Un vehiculo no existe
 *         </li>
 *         <li>
 *             Un usuario y un vehiculo que se supone deben estar vinculados no lo estan.
 *         </li>
 *     </ul>
 *
 * </p>
 */
public class InvalidVehicleException extends Exception{

    public InvalidVehicleException(String message) {
        super(message);
    }
}
