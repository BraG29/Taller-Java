package com.traffic.toll.Interface.impl;

import com.traffic.dtos.vehicle.IdentifierDTO;
import com.traffic.toll.Interface.TollController;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class TollControllerImpl implements TollController {
    @Override
    public Optional<Boolean> isEnabled(IdentifierDTO identifier) {
        return Optional.empty();
    }

    @Override
    public void updateCommonTariff(Double amount) {

    }

    @Override
    public void updatePreferentialTariff(Double amount) {

    }
}
