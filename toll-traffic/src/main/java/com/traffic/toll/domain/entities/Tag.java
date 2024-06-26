package com.traffic.toll.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Toll_Tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "unique_id")
    private UUID uniqueId = UUID.randomUUID();

    public Tag(Long id, UUID uniqueId){
        this.id = id;
        this.uniqueId = uniqueId;
    }

}
