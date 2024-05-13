package com.traffic.dtos.tariff;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class TariffDTO {

    protected Long id;
    protected Double amount;

}
