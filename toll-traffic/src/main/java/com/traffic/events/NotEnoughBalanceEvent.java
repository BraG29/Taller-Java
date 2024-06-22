package com.traffic.events;


import lombok.Getter;

@Getter
public class NotEnoughBalanceEvent extends CustomEvent{

    private Long userId;

    public NotEnoughBalanceEvent(String description, Long userId) {
        super(description);
        this.userId = userId;
    }

    public NotEnoughBalanceEvent() {
    }
}
