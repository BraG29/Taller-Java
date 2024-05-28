package com.traffic.client.domain.tariff;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("preferential")
public class PreferentialTariff extends Tariff{

    public PreferentialTariff(Double amount, Long id) {
        super(amount, id);
    }
}
