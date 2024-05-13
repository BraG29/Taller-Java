package com.traffic.client.domain.tariff;

import lombok.Getter;

@Getter
public class CommonTariff extends Tariff{

    public CommonTariff() {
    }

    public CommonTariff(Double amount, Long id) {
        super(amount, id);
    }
}
