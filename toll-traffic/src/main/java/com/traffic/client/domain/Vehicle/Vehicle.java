package com.traffic.client.domain.Vehicle;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

//@Data
@Getter
@Setter
@Entity(name = "ClientModule_Vehicle")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "Tag_id")
    private Tag tag;

    @OneToMany(mappedBy = "vehicle", fetch = FetchType.EAGER)
    private List<TollPass> tollPass;

    public Vehicle(){
    }

    public Vehicle(Long id, Tag tag, List<TollPass> tollPass) {
        this.id = id;
        this.tag = tag;
        this.tollPass = tollPass;
    }

//    @Override
//    public String toString(){
//        return " [Id: " + id + tag.toString() + tollPass.toString();
//    }


    public void addPass(TollPass pass){

        if(tollPass == null){
            tollPass = new ArrayList<>();
        }
        tollPass.add(pass);

    }

}
