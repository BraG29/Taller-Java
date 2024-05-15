package com.traffic.events;

public class CreditCardRejectedEvent extends CustomEvent{

    public CreditCardRejectedEvent(String description) {
        super(description);
    }

    public CreditCardRejectedEvent() {
    }
}
