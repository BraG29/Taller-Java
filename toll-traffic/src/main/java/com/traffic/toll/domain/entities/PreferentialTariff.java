package com.traffic.toll.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PreferentialTariff extends Tariff {

    public PreferentialTariff(Double amount) {
        super(amount);
    }
}
