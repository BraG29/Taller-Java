package com.traffic.dtos.vehicle;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TagDTO extends IdentifierDTO {

    private String uniqueId;

    public TagDTO(Long id,
                  String uniqueId) {
        super(id);
        this.uniqueId = uniqueId;
    }

    public TagDTO() {
        super();
    }
}
