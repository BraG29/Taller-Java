package com.traffic.dtos.tariff;

import lombok.Data;

@Data
public class TariffDTO {

    protected Double amount;

    public TariffDTO() {
    }

    public TariffDTO(Double amount) {
        this.amount = amount;
    }
}
