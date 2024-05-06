package com.traffic.dtos.tariff;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommonTariffDTO extends TariffDTO{

    public CommonTariffDTO() {
    }

    public CommonTariffDTO(Double amount) {
        super(amount);
    }
}
