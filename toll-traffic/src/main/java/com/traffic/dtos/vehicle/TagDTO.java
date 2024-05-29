package com.traffic.dtos.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TagDTO extends IdentifierDTO {

    private Long uniqueId;
    private String UUID;



}
