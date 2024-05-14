package com.traffic.exceptions;

/**
 * Excepcion pensada para lanzarse cuando haya un error interno del servidor
 * Ej: Si no hay un precio setteado para alguna tarifa
 */
public class InternalErrorException extends Exception {

    public InternalErrorException(String message) {
        super(message);
    }
}
