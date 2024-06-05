package com.traffic.toll.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

//Lombok
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

//JPA
@Entity(name = "Toll_Vehicle")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected Long id;
    @OneToOne
    @JoinColumn(name = "tag", unique = true)
    protected Tag tag;

}
