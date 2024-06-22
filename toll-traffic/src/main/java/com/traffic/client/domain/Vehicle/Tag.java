package com.traffic.client.domain.Vehicle;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity(name = "ClientModule_Tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private UUID uniqueId = UUID.randomUUID();

    public Tag(){
    }

    public Tag(Long tag, UUID uniqueId){
        this.id = tag;
        this.uniqueId = uniqueId;
    }

    @Override
    public String toString() {
        return "Tag [" + id + "]";
    }
}
