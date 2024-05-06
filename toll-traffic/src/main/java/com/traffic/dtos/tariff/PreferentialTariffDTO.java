package com.traffic.dtos.tariff;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PreferentialTariffDTO extends TariffDTO{

    public PreferentialTariffDTO() {
    }

    public PreferentialTariffDTO(Double amount) {
        super(amount);
    }
}
