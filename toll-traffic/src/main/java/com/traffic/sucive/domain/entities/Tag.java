package com.traffic.sucive.domain.entities;

import com.traffic.dtos.vehicle.TagDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Sucive_Tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "unique_id")
    private UUID uniqueId = UUID.randomUUID();

    public Tag(Long id){
            this.id = id;
        }

    public TagDTO toDTO(){
            return new TagDTO(id, uniqueId.toString());
    }
}