package com.traffic.sucive.domain.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name ="Sucive_CommonTariff")
@DiscriminatorValue("common")
public class CommonTariff extends Tariff{


    public CommonTariff(Double amount, Long id) {
        super(amount, id);
    }
}