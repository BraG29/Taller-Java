package com.traffic.toll.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommonTariff extends Tariff{

    public CommonTariff(Double amount) {
        super(amount);
    }
}
