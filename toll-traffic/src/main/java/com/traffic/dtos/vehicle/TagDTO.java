package com.traffic.dtos.vehicle;

import lombok.Data;

@Data
public class TagDTO {

    private Long uniqueId;

    public TagDTO() {
    }

    public TagDTO(Long uniqueId) {
        this.uniqueId = uniqueId;
    }
}
