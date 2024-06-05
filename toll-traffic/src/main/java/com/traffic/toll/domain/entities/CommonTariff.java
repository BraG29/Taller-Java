package com.traffic.toll.domain.entities;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("common")
public class CommonTariff extends Tariff{

    public CommonTariff(Long id, Double amount) {
        super(id, amount);
    }
}
