package com.traffic.events;

import com.traffic.dtos.vehicle.TollPassDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SucivePaymentEvent extends CustomEvent{

    private TollPassDTO tollPassDTO;

    public SucivePaymentEvent(String description) {
        super(description);
    }

}
