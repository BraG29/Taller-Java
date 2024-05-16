package com.traffic.client.domain.tariff;


import lombok.Getter;

@Getter
public class PreferentialTariff extends Tariff{

    public PreferentialTariff(){}

    public PreferentialTariff(Double amount, Long id) {
        super(amount, id);
    }
}
