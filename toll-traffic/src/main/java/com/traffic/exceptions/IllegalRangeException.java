package com.traffic.exceptions;

/**
 * <p>
 *     Excepcion que se lanza cuando el rango de fechas es incorrecto
 *     y la fecha de inicio es mas grande que la fecha de fin
 * </p>
 */
public class IllegalRangeException extends Exception{
    public IllegalRangeException(String message) {
    }
}
