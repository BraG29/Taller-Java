package com.traffic.client.domain.User;

import com.traffic.client.domain.tariff.CommonTariff;
import lombok.Getter;

@Getter
public class SuciveCustomer {

    private Long id;
    private CommonTariff tariff;

    public SuciveCustomer(){}

    public SuciveCustomer(Long id, CommonTariff tariff) {
        this.id = id;
        this.tariff = tariff;
    }
}
