package com.traffic.events;

public class NotEnoughBalanceEvent extends CustomEvent{

    public NotEnoughBalanceEvent(String description) {
        super(description);
    }

    public NotEnoughBalanceEvent() {
    }
}
