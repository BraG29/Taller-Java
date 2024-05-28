package com.traffic.toll.domain.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("preferential")
public class PreferentialTariff extends Tariff {

    public PreferentialTariff(Long id, Double amount) {
        super(id, amount);
    }
}
