package com.traffic.toll.application.impl;

import com.traffic.client.Interface.local.ClientController;
import com.traffic.dtos.account.AccountDTO;
import com.traffic.dtos.account.PostPayDTO;
import com.traffic.dtos.account.PrePayDTO;
import com.traffic.dtos.vehicle.IdentifierDTO;
import com.traffic.dtos.vehicle.LicensePlateDTO;
import com.traffic.dtos.vehicle.TagDTO;
import com.traffic.events.CustomEvent;
import com.traffic.events.VehiclePassEvent;
import com.traffic.exceptions.*;
import com.traffic.sucive.Interface.SuciveController;
import com.traffic.toll.Interface.local.TollService;
import com.traffic.toll.application.PaymentService;
import com.traffic.toll.application.VehicleService;
import com.traffic.toll.domain.entities.*;
import com.traffic.toll.domain.repositories.IdentifierRepository;
import com.traffic.toll.domain.repositories.TariffRepository;
import com.traffic.toll.domain.repositories.VehicleRepository;
import com.traffic.toll.infraestructure.messaging.SendMessageQueueUtil;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ApplicationScoped
public class TollServiceImpl implements TollService {

    @Inject
    private TariffRepository tariffRepository;
    @Inject
    private PaymentService paymentService;
    @Inject
    private VehicleService vehicleService;
    @Inject
    private Event<CustomEvent> event;
    @Inject
    private SendMessageQueueUtil messageQueueUtil;

    private void fireTollPass(String message){
        event.fire(new VehiclePassEvent(message));
    }

    @Override
    public Optional<Boolean> isEnabled(IdentifierDTO identifier) throws IllegalArgumentException,
            InternalErrorException,
            InvalidVehicleException{

        if (identifier == null) {
            throw new IllegalArgumentException("Identifier es null");
        }

        Optional<Vehicle> vehicleOPT = vehicleService.getByIdentifier(identifier);

        if (vehicleOPT.isPresent()) {
            Vehicle vehicle = vehicleOPT.get();
            final String TOLL_PASS_MESSAGE = "Se habilito la pasada para el vehiculo de tag: %s";

            Optional<Tariff> preferentialTariffOPT = tariffRepository.findTariff(PreferentialTariff.class);
            PreferentialTariff preferentialTariff = (PreferentialTariff) preferentialTariffOPT.orElseThrow(() ->
                    new InternalErrorException("No hay tarifa preferencial")
            );

            Optional<Tariff> commonTariffOPT = tariffRepository.findTariff(CommonTariff.class);
            CommonTariff commonTariff = (CommonTariff) commonTariffOPT.orElseThrow(() ->
                    new InternalErrorException("No hay tarifa comun")
            );

            if (vehicle instanceof NationalVehicle) {
                messageQueueUtil.sendMessage(vehicle.getTag(), commonTariff, preferentialTariff);
                this.fireTollPass(TOLL_PASS_MESSAGE.formatted(vehicle.getTag().getUniqueId()));
                return Optional.of(true);
            }


            if (paymentService.tryPayment(vehicle, preferentialTariff)) {
                this.fireTollPass(TOLL_PASS_MESSAGE.formatted(vehicle.getTag().getUniqueId()));
                return Optional.of(true);
            }

        }else{
          throw new InvalidVehicleException("No existe vehiculo con esa identificacion");
        }

        return Optional.of(false);
    }

    @Override
    public void updateCommonTariff(Double amount) {

        if( amount < 0 ){
            throw new IllegalArgumentException("El monto no es valido");
        }

        Tariff tariff = tariffRepository.findTariff(CommonTariff.class).orElseThrow();
        tariff.setAmount(amount);
        tariffRepository.save(tariff)
                .orElseThrow(() -> new PersistenceException(""));
    }

    @Override
    public void updatePreferentialTariff(Double amount) throws NoSuchElementException {

        if (amount < 0) {
            throw new IllegalArgumentException("El monto no es valido");
        }

        Tariff tariff = tariffRepository.findTariff(PreferentialTariff.class).orElseThrow();
        tariff.setAmount(amount);
        tariffRepository.save(tariff)
                .orElseThrow(() -> new PersistenceException(""));
    }
}
