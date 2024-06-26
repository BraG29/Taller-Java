package com.traffic.sucive.Interface.impl;

import com.traffic.dtos.PaymentTypeData;
import com.traffic.dtos.vehicle.LicensePlateDTO;
import com.traffic.dtos.vehicle.NationalVehicleDTO;
import com.traffic.dtos.vehicle.TagDTO;
import com.traffic.dtos.vehicle.VehicleDTO;
import com.traffic.events.VehicleAddedEvent;
import com.traffic.exceptions.ExternalApiException;
import com.traffic.exceptions.InternalErrorException;
import com.traffic.exceptions.InvalidVehicleException;
import com.traffic.sucive.Interface.SuciveController;
import com.traffic.sucive.domain.entities.*;
import com.traffic.sucive.domain.repository.SuciveRepository;
//import com.traffic.sucive.domain.vehicle.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import okhttp3.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class SuciveControllerImpl implements SuciveController {

    @Inject
    private SuciveRepository repository;

    @Override
    public void vehicleRegistration(@Observes VehicleAddedEvent vehicleEvent) throws Exception {

        //we get the National Vehicle and cast it from the event
        NationalVehicleDTO vehicleDTO = (NationalVehicleDTO) vehicleEvent.getVehicle();

        //we get the TagDTO from the vehicleDTO
        TagDTO tagDTO = vehicleDTO.getTagDTO();

        //we get the LicenseDTO from the vehicleDTO
        LicensePlateDTO licensePlateDTO = vehicleDTO.getLicensePlateDTO();

        //we form the Tag from the TagDTO
        Tag tagToAdd = new Tag(tagDTO.getId());

        //we form the plate from the LicensePlateDTO
        LicensePlate plateToAdd = new LicensePlate(licensePlateDTO.getId(), licensePlateDTO.getLicensePlateNumber());


        NationalVehicle vehicleToAdd = new NationalVehicle(vehicleDTO.getId(), tagToAdd, null,plateToAdd) ;

        try{
            repository.addVehicle(vehicleToAdd);

        }catch (Exception e){
            System.err.println(e.getMessage());
            throw e;
        }
    }

    @Override
    public void notifyPayment(LicensePlateDTO licensePlate, Double amount)  throws ExternalApiException, IllegalArgumentException, InvalidVehicleException {

        OkHttpClient client = new OkHttpClient();

        String json = "{\"id\":0,\"licensePlateNumber\":\""+licensePlate.getLicensePlateNumber()+"\"}";
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url("http://localhost:8081/sucive-service/api/controller/checkSucive/")
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());

            if (response.code() == 200){
                repository.updateVehicleTollPass(licensePlate, amount);
            }else {
                //ask los pibes if I should be throwing some event here
                throw new ExternalApiException("No se pudo validar la compra sucive");
            }
        } catch (Exception e) {
            throw new ExternalApiException(e.getMessage());
        }
    }

    @Override
    public Optional<List<Double>> paymentInquiry(LocalDate from, LocalDate to) {

        //I get all the sucive client's toll passes
        ArrayList<TollPass> tollPasses = (ArrayList<TollPass>) repository.getAllTollPasses();
        ArrayList<Double> allPayments = new ArrayList<>();

        for (TollPass toll : tollPasses){

            //if the toll pass is between the 2 dates AND is sucive
            if ( toll.getPassDate().isAfter(from)
                    && toll.getPassDate().isBefore(to)
                    && toll.getPaymentType() == PaymentTypeData.SUCIVE){

                allPayments.add(toll.getCost());
            }
        }

        if (allPayments.isEmpty()){
            return Optional.empty();
        }else{
            return Optional.of(allPayments);
        }
    }



    @Override
    public Optional<List<Double>> paymentInquiry(LicensePlateDTO licensePlate) throws InvalidVehicleException {

        try {
            //we ask the DB for the national vehicle to get the toll passes from
            NationalVehicle vehicle =  repository.findVehicleByLicensePlate(licensePlate);

            //we get the toll passes from the vehicle
            ArrayList<TollPass> tollPasses = (ArrayList<TollPass>) vehicle.getTollPass();

            //array that I will eventually return with the payments
            ArrayList<Double> allPayments = new ArrayList<>();


            for (TollPass toll :tollPasses ){ //for each toll pass we found
                allPayments.add(toll.getCost());// add it's payment to the array
            }

            if (allPayments.isEmpty()){
                return Optional.empty();
            }else{
                return Optional.of(allPayments);
            }

        } catch (InternalErrorException e) {
            throw new InvalidVehicleException(e.getMessage());
        }
    }



}
