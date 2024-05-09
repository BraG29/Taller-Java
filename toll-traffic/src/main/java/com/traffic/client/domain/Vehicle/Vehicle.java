package com.traffic.client.domain.Vehicle;

import lombok.Data;
import java.util.List;

@Data
public class Vehicle {

    private Tag tag;
    private List<TollPass> tollPass;

    public Vehicle(){
    }

    public Vehicle(Tag tag, List<TollPass> tollPass) {
        this.tag = tag;
        this.tollPass = tollPass;
    }
}
