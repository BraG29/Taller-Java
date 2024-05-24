package com.traffic.client.domain.Vehicle;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class Vehicle {

    private Long id;
    private Tag tag;
    private List<TollPass> tollPass;

    public Vehicle(){
    }

    public Vehicle(Long id, Tag tag, List<TollPass> tollPass) {
        this.id = id;
        this.tag = tag;
        this.tollPass = tollPass;
    }

    @Override
    public String toString(){
        return " [Id: " + id + tag.toString() + tollPass.toString();
    }


    public void addPass(TollPass pass){

        if(tollPass == null){
            tollPass = new ArrayList<>();
        }
        tollPass.add(pass);

    }

}
