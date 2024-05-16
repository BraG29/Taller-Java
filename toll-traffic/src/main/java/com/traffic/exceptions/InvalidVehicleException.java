package com.traffic.exceptions;

/**
 * <p>
 *     Excepcion que se lanza cuando un usuario y un vehiculo que se supone
 *     deben estar vinculados no lo estan.
 * </p>
 */
public class InvalidVehicleException extends Exception{

    public InvalidVehicleException(String message) {
        super(message);
    }
}
