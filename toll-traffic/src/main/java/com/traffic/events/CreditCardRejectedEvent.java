package com.traffic.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CreditCardRejectedEvent extends CustomEvent{

    private long userId;
    public CreditCardRejectedEvent(String description, long userId) {
        super(description);
        this.userId = userId;
    }

    public CreditCardRejectedEvent() {
    }
}
