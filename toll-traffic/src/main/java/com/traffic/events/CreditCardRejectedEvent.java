package com.traffic.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CreditCardRejectedEvent extends CustomEvent{

    private Long userId;
    public CreditCardRejectedEvent(String description, Long userId) {
        super(description);
        this.userId = userId;
    }

    public CreditCardRejectedEvent() {
    }
}
