package com.traffic.toll.application.impl;

import com.traffic.client.Interface.local.ClientController;
import com.traffic.dtos.account.AccountDTO;
import com.traffic.dtos.account.PostPayDTO;
import com.traffic.dtos.account.PrePayDTO;
import com.traffic.dtos.vehicle.LicensePlateDTO;
import com.traffic.dtos.vehicle.TagDTO;
import com.traffic.exceptions.ExternalApiException;
import com.traffic.exceptions.InternalErrorException;
import com.traffic.exceptions.InvalidVehicleException;
import com.traffic.sucive.Interface.SuciveController;
import com.traffic.toll.application.PaymentService;
import com.traffic.toll.domain.entities.*;
import com.traffic.toll.domain.repositories.TariffRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ApplicationScoped
public class PaymentServiceImpl implements PaymentService {
    @Inject
    ClientController clientController;
    @Inject
    SuciveController suciveController;


    @Override
    public boolean tryPayment(Vehicle vehicle,
                              PreferentialTariff preferentialTariff){

        TagDTO tagDTO = new TagDTO(
                vehicle.getTag().getId(),
                vehicle.getTag().getUniqueId().toString());

        Optional<List<AccountDTO>> accountsOPT = clientController.getAccountByTag(tagDTO);

        List<AccountDTO> accountDTOS = new ArrayList<>();

        try {
            accountDTOS = accountsOPT.orElseThrow();

        } catch (NoSuchElementException e) {
            System.err.println("El cliente no tiene cuentas");
            return false;
        }

        if (accountDTOS.stream().anyMatch(a -> a instanceof PrePayDTO)) {

            PrePayDTO prePayAccount = (PrePayDTO) accountDTOS.stream()
                    .filter(a -> a instanceof PrePayDTO)
                    .findFirst()
                    .get();

            if (prePayAccount.getBalance() >= preferentialTariff.getAmount()) {
                try{
                    clientController.prePay(preferentialTariff.getAmount(), tagDTO);
                    return true;

                }catch (Exception e){
                    System.err.println("No se pudo concretar el pago por PrePaga: " + e.getMessage());
                }

            }else {
                try {
                    clientController.throwEvent(tagDTO);
                }catch (Exception e) {
                    System.err.println("Error al lanzar el evento de saldo insuficiente: " + e.getMessage());
                }
            }
        }

        if (accountDTOS.stream().anyMatch(a -> a instanceof PostPayDTO)) {
            try {
                clientController.postPay(preferentialTariff.getAmount(), tagDTO);
                return true;

            }catch (Exception e){
                System.out.println("No se pudo concretar el pago por PostPaga: " + e.getMessage());
            }
        }

        return false;
    }

    @Override
    public boolean trySucive(NationalVehicle vehicle, CommonTariff commonTariff){
        LicensePlate licensePlate = vehicle.getLicensePlate();

        LicensePlateDTO licensePlateDTO = new LicensePlateDTO(
                licensePlate.getId(),
                licensePlate.getLicensePlateNumber());

        try {
            suciveController.notifyPayment(licensePlateDTO,
                    commonTariff.getAmount());

        } catch (ExternalApiException e) {
            System.err.println("Fallo el pago por Sucive: " + e.getMessage());
            return false;
        } catch (InvalidVehicleException e) {
            System.err.printf("Modulo Sucive no tiene registrado el vehiculo de matricula: %s%n",
                    licensePlate.getLicensePlateNumber());
            return false;
        }

        return true;
    }
}
