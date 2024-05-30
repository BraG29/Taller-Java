package com.traffic.sucive.domain.vehicle;

import com.traffic.sucive.domain.vehicle.Tag;
import com.traffic.sucive.domain.vehicle.TollPass;
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



