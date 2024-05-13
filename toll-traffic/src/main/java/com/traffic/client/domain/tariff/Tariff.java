package com.traffic.client.domain.tariff;


import lombok.Getter;

@Getter
public abstract class Tariff {

    protected Long id;
    protected Double amount;

    public Tariff(){}

    public Tariff(Double amount, Long id) {
        this.amount = amount;
        this.id = id;
    }
}
