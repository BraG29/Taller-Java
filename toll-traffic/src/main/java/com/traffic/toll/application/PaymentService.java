package com.traffic.toll.application;

import com.traffic.exceptions.InternalErrorException;
import com.traffic.toll.domain.entities.CommonTariff;
import com.traffic.toll.domain.entities.NationalVehicle;
import com.traffic.toll.domain.entities.PreferentialTariff;
import com.traffic.toll.domain.entities.Vehicle;

import java.util.List;
import java.util.NoSuchElementException;

public interface PaymentService {

    public boolean tryPayment(Vehicle vehicle,
                              PreferentialTariff preferentialTariff);

    public boolean trySucive(NationalVehicle vehicle,
                             CommonTariff commonTariff);

}
