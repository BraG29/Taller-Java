package com.traffic.dtos.vehicle;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TagDTO extends IndetifierDTO{

    private Long uniqueId;

    public TagDTO() {
    }

    public TagDTO(Long uniqueId) {
        this.uniqueId = uniqueId;
    }
}
