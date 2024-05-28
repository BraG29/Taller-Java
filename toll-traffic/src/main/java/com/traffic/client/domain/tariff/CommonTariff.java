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
@DiscriminatorValue("common")
public class CommonTariff extends Tariff{

    public CommonTariff(Double amount, Long id) {
        super(amount, id);
    }
}
