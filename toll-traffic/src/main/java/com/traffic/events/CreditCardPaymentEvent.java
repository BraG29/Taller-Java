package com.traffic.events;

public class CreditCardPaymentEvent extends CustomEvent{

    public CreditCardPaymentEvent(String description) {
        super(description);
    }

    public CreditCardPaymentEvent() {
    }
}
