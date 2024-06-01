package com.traffic.exceptions;

/**
 * <p>
 *     Excepcion que se lanzara cuando una API externa lanze un error
 * </p>
 */
public class ExternalApiException extends Exception{
    public ExternalApiException(String message) {
        super(message);
    }
}
