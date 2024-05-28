package com.traffic.client.domain.Vehicle;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity(name = "ClientModule_Tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    private UUID UID = UUID.randomUUID();

    public Tag(){
    }

    public Tag(Long tag){
        this.tagId = tag;
    }

    @Override
    public String toString() {
        return "Tag [" + tagId + "]";
    }
}
