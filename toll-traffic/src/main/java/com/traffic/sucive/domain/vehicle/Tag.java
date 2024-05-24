package com.traffic.sucive.domain.vehicle;

import com.traffic.sucive.domain.vehicle.Identifier;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Tag extends Identifier {
    private Long uniqueId;

}