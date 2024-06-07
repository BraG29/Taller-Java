package com.traffic.events;


import lombok.Getter;

@Getter
public class NotEnoughBalanceEvent extends CustomEvent{

    public NotEnoughBalanceEvent(String description) {
        super(description);
    }

    public NotEnoughBalanceEvent() {
    }
}
