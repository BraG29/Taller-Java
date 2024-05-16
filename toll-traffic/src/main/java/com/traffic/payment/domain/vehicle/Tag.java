package com.traffic.payment.domain.vehicle;

import com.traffic.dtos.vehicle.IdentifierDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Tag extends Identifier {
    private Long uniqueId;

}