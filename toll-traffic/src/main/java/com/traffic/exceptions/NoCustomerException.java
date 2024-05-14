package com.traffic.exceptions;

/**
 * <p>
 *     Excepcion lanzada cuando un usuario que se supone debe ser
 *     cliente del telepeaje no lo es
 * </p>
 */
public class NoCustomerException extends Exception{

    public NoCustomerException(String message) {
        super(message);
    }

    public NoCustomerException(){
        super();
    }

}
