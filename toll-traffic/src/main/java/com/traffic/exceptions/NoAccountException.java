package com.traffic.exceptions;

/**
 * <p>
 *     Excepcion lanzada cuando un usuario no dispone de cuenta PrePaga O PostPaga
 * </p>
 */
public class NoAccountException extends Exception{
    public NoAccountException(String message) {
    }
}
