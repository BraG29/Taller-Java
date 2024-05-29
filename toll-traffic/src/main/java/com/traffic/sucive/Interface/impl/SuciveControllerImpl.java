package com.traffic.sucive.Interface.impl;

import com.traffic.dtos.PaymentTypeData;
import com.traffic.dtos.vehicle.LicensePlateDTO;
import com.traffic.exceptions.ExternalApiException;
import com.traffic.exceptions.InvalidVehicleException;
import com.traffic.sucive.Interface.SuciveController;
import com.traffic.sucive.domain.entities.*;
import com.traffic.sucive.domain.repository.SuciveRepository;
//import com.traffic.sucive.domain.vehicle.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import okhttp3.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SuciveControllerImpl implements SuciveController {

    @Inject
    private SuciveRepository repository;

    @Override
    public void notifyPayment(LicensePlateDTO licensePlate, Double amount)  throws ExternalApiException, IllegalArgumentException, InvalidVehicleException {

        //TODO call the mock API for Sucive 👁👄👁

        OkHttpClient client = new OkHttpClient();

        String json = "{\"id\":0,\"licensePlateNumber\":\""+licensePlate.getLicensePlateNumber()+"\"}";
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url("http://localhost:8080/sucive-service/api/controller/suciveCheck/")
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());

            if (response.code() == 200){
                repository.updateVehicleTollPass(licensePlate, amount);
            }else {
                throw new ExternalApiException("No se pudo validar la compra sucive");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Optional<List<Double>> paymentInquiry(LocalDate from, LocalDate to) {

        //I get all the sucive clients
        ArrayList<User> users = (ArrayList<User>) repository.getAllUsers();
        ArrayList<Double> allPayments = new ArrayList<>();

        //for each user from the BDD
        for (User user : users){
            //I get all the links from the user
            ArrayList<Link> userLinks = (ArrayList<Link>) user.getLinkedCars();

            //for each link the given user has
            for (Link link : userLinks){

                //I get the vehicle from the link
                Vehicle vehicle = link.getVehicle();

                //I get all the Toll passes from the vehicle
                ArrayList<TollPass> tollPasses = (ArrayList<TollPass>) vehicle.getTollPass();

                //for each toll pass registered in any given vehicle
                for (TollPass toll : tollPasses){

                    //if the toll pass is between the 2 dates AND is sucive
                    if ( toll.getPassDate().isAfter(from) && toll.getPassDate().isBefore(to) && toll.getPaymentType() == PaymentTypeData.SUCIVE){
                        allPayments.add(toll.getCost());
                    }
                }
            }
        }//everyday that passes, we stray further from God. . .


        if (allPayments.isEmpty()){
            return Optional.empty();
        }else{
            return Optional.of(allPayments);
        }
    }



    @Override
    public Optional<List<Double>> paymentInquiry(LicensePlateDTO licensePlate) {

        ArrayList<User> users = (ArrayList<User>) repository.getAllUsers(); //I get my users TODO: replace with proper BDD implementation

        LicensePlate licenseToCheck = new LicensePlate(licensePlate.getId(), licensePlate.getLicensePlateNumber());//transform DTO to Domain Object

        ArrayList<Double> allPayments = new ArrayList<>();//array that I will eventually return

        for (User user : users){//for each user I get

            for (Link link : user.getLinkedCars()){//for each linked Vehicle

                if (link.getVehicle() instanceof NationalVehicle){ //I check only for National Vehicles

                    if(( (NationalVehicle) link.getVehicle()).getPlate().getLicensePlateNumber().equalsIgnoreCase(licenseToCheck.getLicensePlateNumber())){ //I make sure I am on the vehicle with the correct License Plate

                        for (TollPass toll : link.getVehicle().getTollPass()){ //for all the toll passes for the vehicle with the correct License Plate

                            if ( toll.getPaymentType() == PaymentTypeData.SUCIVE){ //if the payment is Sucive
                                allPayments.add(toll.getCost());
                            }
                        }
                    }
                }


            }
        }

        if (allPayments.isEmpty()){
            return Optional.empty();
        }else{
            return Optional.of(allPayments);
        }
    }



}
