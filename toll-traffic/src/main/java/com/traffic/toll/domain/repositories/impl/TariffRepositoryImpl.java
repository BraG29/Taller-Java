package com.traffic.toll.domain.repositories.impl;

import com.traffic.toll.domain.entities.CommonTariff;
import com.traffic.toll.domain.entities.PreferentialTariff;
import com.traffic.toll.domain.entities.Tariff;
import com.traffic.toll.domain.repositories.TariffRepository;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TariffRepositoryImpl implements TariffRepository {

    private List<Tariff> tariffList;

    @PostConstruct
    public void initTariffList(){
        tariffList = List.of(
                new PreferentialTariff(80D),
                new CommonTariff(100D)
        );
    }

    @Override
    public Optional<Tariff> findCommonTariff() {
        return tariffList.stream()
                .filter(tariff -> tariff instanceof CommonTariff)
                .findFirst();
    }

    @Override
    public Optional<Tariff> findPreferentialTariff() {
        return tariffList.stream()
                .filter(tariff -> tariff instanceof PreferentialTariff)
                .findFirst();
    }

    @Override
    public void updateCommonTariff(Double amount) {
        tariffList.stream()
                .filter( tariff ->  tariff instanceof CommonTariff)
                .findFirst()
                .get()
                .setAmount(amount);
    }

    @Override
    public void updatePreferentialTariff(Double amount) {
        tariffList.stream()
                .filter( tariff ->  tariff instanceof PreferentialTariff)
                .findFirst()
                .get()
                .setAmount(amount);

    }
}
