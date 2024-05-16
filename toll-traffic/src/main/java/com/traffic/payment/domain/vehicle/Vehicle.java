package com.traffic.payment.domain.vehicle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public abstract class Vehicle {

    protected Long id;
    protected List<TollPass> tollPass;
    protected Tag tag;

}



