package com.traffic.toll.domain.repositories;

import com.traffic.toll.domain.entities.Tariff;

import java.util.Optional;

public interface TariffRepository {

    public Optional<Tariff> findCommonTariff();
    public Optional<Tariff> findPreferentialTariff();
    public void updateCommonTariff(Double amount);
    public void updatePreferentialTariff(Double amount);

}
