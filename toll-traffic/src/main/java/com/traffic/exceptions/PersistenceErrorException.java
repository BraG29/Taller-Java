package com.traffic.exceptions;

/**
 * <p>
 *     Excepcion que se lanzara cuando haya errores a la hora de persisitir datos
 * </p>
 */
public class PersistenceErrorException extends Exception{

    public PersistenceErrorException(String message) {
        super(message);
    }
}
