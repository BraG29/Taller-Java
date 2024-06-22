package com.traffic.payment.Interface.impl;

import com.traffic.dtos.PaymentTypeData;
import com.traffic.dtos.account.CreditCardDTO;
import com.traffic.dtos.account.PostPayDTO;
import com.traffic.dtos.account.PrePayDTO;
import com.traffic.dtos.user.NationalUserDTO;
import com.traffic.dtos.user.TollCustomerDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.dtos.vehicle.*;
import com.traffic.events.CreditCardRejectedEvent;
import com.traffic.events.NewUserEvent;
import com.traffic.exceptions.ExternalApiException;
import com.traffic.exceptions.InternalErrorException;
import com.traffic.exceptions.InvalidVehicleException;
import com.traffic.exceptions.NoCustomerException;
import com.traffic.payment.Interface.PaymentController;
//import com.traffic.payment.domain.account.CreditCard;
//import com.traffic.payment.domain.account.PostPay;
//import com.traffic.payment.domain.account.PrePay;
import com.traffic.payment.domain.entities.*;
import com.traffic.payment.domain.repository.PaymentRepository;
//import com.traffic.payment.domain.user.TollCustomer;
//import com.traffic.payment.domain.user.User;
//import com.traffic.payment.domain.vehicle.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.transaction.Transactional;
import okhttp3.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PaymentControllerImpl implements PaymentController {

    @Inject
    private PaymentRepository repository;

    @Inject
    private Event<CreditCardRejectedEvent> creditCardRejectedEventPublisher;


    @Transactional
    @Override
    public void customerRegistration(@Observes NewUserEvent userEvent) throws ExternalApiException, NoCustomerException, InternalErrorException {


        //TODO would be to move my DTO to Domain object functions to my Domain classes
        //I get the userDTO from the event that called this function
        UserDTO user = userEvent.getUser();

        //Lucas left me this example so I can parse the now string value of the dates
        //LocalDate cardDate = LocalDate.parse(creditCard.getExpireDate(), DateTimeFormatter.ISO_DATE);

        //I prepare the DTOs to be turned into domain objects
        // creditCard > pre & postPay > tollCustomer
        TollCustomerDTO tollCustomerDTO = user.getTollCustomer();
        PostPayDTO postPayDTO = tollCustomerDTO.getPostPayDTO();
        PrePayDTO prePayDTO = tollCustomerDTO.getPrePayDTO();

        //I get the credit card from the postPayDTO
        CreditCard card = new CreditCard(null,
                                                                postPayDTO.getCreditCardDTO().getCardNumber(),
                                                                postPayDTO.getCreditCardDTO().getName(),
                                                                LocalDate.parse(postPayDTO.getCreditCardDTO().getExpireDate(), DateTimeFormatter.ISO_DATE));

        //postPayDTO to postPay
        POSTPay postPay =  new POSTPay(null,postPayDTO.getAccountNumber(),postPayDTO.getCreationDate(), card );

        //prePayDTO to prePay
        PREPay prePay = new PREPay(null,prePayDTO.getAccountNumber(),
                                                    prePayDTO.getCreationDate(),
                                                    prePayDTO.getBalance());

        //to prepare tollCustomer, I need postPay & prePay
        TollCustomer tollCustomer = new TollCustomer(null,postPay, prePay);

        //list of LinkDTOs from the user
        List <LinkDTO> vehicleLinksDTOs = user.getLinkedVehicles();

        //list of the vehicle links we will be getting from the vehicleLinksDTOs
        ArrayList<Link> vehicleLinks = new ArrayList<>();

        for (LinkDTO link  : vehicleLinksDTOs){

            ArrayList<TollPass> passes = new ArrayList<>(); //Domain Object list of all TollPases of a given vehicle

            if (link.getVehicle() instanceof ForeignVehicleDTO){

                ForeignVehicleDTO vehicleDTO = (ForeignVehicleDTO) link.getVehicle();

                //I get the list of toll passes DTOs for the vehicle of the given Link
                ArrayList<TollPassDTO> tollPassListToIterate = (ArrayList<TollPassDTO>) vehicleDTO.getTollPassDTO();

                //Iterate through it to get the Domain object list of toll pases for the given vehicle
                for (TollPassDTO toll : tollPassListToIterate){

                    LocalDate tollDate = LocalDate.parse(toll.getDate(), DateTimeFormatter.ISO_DATE);

                    TollPass tollToAdd = new TollPass(null,tollDate,toll.getCost(),toll.getPaymentType());
                    passes.add(tollToAdd);
                }

                //transform TagDTO to Tag
                Tag tagToAdd = new Tag(vehicleDTO.getId());
                ForeignVehicle vehicleToAdd = new ForeignVehicle(vehicleDTO.getId(),tagToAdd); //used to have passes. . .

                //I need a vehicle domain object in order to create my link
                Link linkToAdd = new Link(link.getId(), link.getActive() ,vehicleToAdd, link.getInitialDate());

                vehicleLinks.add(linkToAdd);

            }else if (link.getVehicle() instanceof NationalVehicleDTO){ //we do the same but for NationalVehicle

                NationalVehicleDTO vehicleDTO = (NationalVehicleDTO) link.getVehicle();

                //I get the list of toll passes DTOs for the vehicle of the given Link
                ArrayList<TollPassDTO> tollPassListToIterate = (ArrayList<TollPassDTO>) vehicleDTO.getTollPassDTO();

                //Iterate through it to get the Domain object list of toll pases for the given vehicle
                for (TollPassDTO toll : tollPassListToIterate){

                    LocalDate tollDate = LocalDate.parse(toll.getDate(), DateTimeFormatter.ISO_DATE);

                    TollPass tollToAdd = new TollPass(null, tollDate,toll.getCost(),toll.getPaymentType());
                    passes.add(tollToAdd);
                }

                //transform TagDTO to Tag
                Tag tagToAdd = new Tag(vehicleDTO.getId());

                LicensePlate licenseToAdd = new LicensePlate(  vehicleDTO.getLicensePlateDTO().getId(), vehicleDTO.getLicensePlateDTO().getLicensePlateNumber() );

                NationalVehicle vehicleToAdd = new NationalVehicle(vehicleDTO.getId(),tagToAdd , passes , licenseToAdd);

                //I need a vehicle domain object in order to create my link
                Link linkToAdd = new Link(link.getId(), link.getActive(), vehicleToAdd, link.getInitialDate());

                vehicleLinks.add(linkToAdd);
            }else{
                throw new InternalErrorException("No hay ningún vehiculo registrado para el usuario: " + user.getName());
            }
        }


        if(user instanceof NationalUserDTO){

            User userToAdd = new NationalUser(vehicleLinks,
                    tollCustomer,
                    user.getCi(),
                    user.getName(),
                    user.getPassword(),
                    user.getEmail(),
                    user.getId());

            //we persist the data
            repository.addUser(userToAdd);

        }else{

            User userToAdd = new ForeignUser(vehicleLinks,
                    tollCustomer,
                    user.getCi(),
                    user.getName(),
                    user.getPassword(),
                    user.getEmail(),
                    user.getId());

            //we persist the data
            repository.addUser(userToAdd);
        }


    }

    @Override
    @Transactional
    public void notifyPayment(UserDTO user,
                              VehicleDTO vehicle,
                              Double amount,
                              CreditCardDTO creditCard) throws ExternalApiException, NoCustomerException, IllegalArgumentException {

        //check for the customer to be properly initialized
        if (user.getTollCustomer() == null){
            throw new NoCustomerException("El cliente no está registrado a Telepeaje");
        }

        LocalDate cardDate = LocalDate.parse(creditCard.getExpireDate(), DateTimeFormatter.ISO_DATE);
        if (cardDate.isBefore(LocalDate.now())){
            throw new IllegalArgumentException("La tarjeta de crédito está vencida");
        }

        OkHttpClient client = new OkHttpClient();

        //String json = "{\"name\":"+user.getName()+",\"cardNumber\":\"\"}";
        JsonObject json =
                Json.createObjectBuilder().add("name", user.getName())
                .add("cardNumber", creditCard.getCardNumber())
                        .build();

        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url("http://localhost:8081/payment-service/api/controller/paymentCheck/")
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println("Respuesta API externa: " + response.body().string());

            if (response.code() == 200){
                repository.addTollPassToUserVehicle(user, vehicle, amount, creditCard);
            }else {
                //card rejected con ID usuario
                CreditCardRejectedEvent eventToFire = new CreditCardRejectedEvent("La tarjeta "
                                                                                                                            + creditCard.getCardNumber()
                                                                                                                            + " ha sido rechazada",
                                                                                                                            user.getId());

                creditCardRejectedEventPublisher.fire(eventToFire);

                throw new ExternalApiException("No se pudo validar la compra Post Paga");
            }
        } catch (Exception e) {
            throw new ExternalApiException(e.getMessage());
        }

    }

    @Override
    public Optional<List<Double>> paymentInquiry(LocalDate from, LocalDate to) {

        //we get all the Toll passes from the repository
        ArrayList<TollPass> tollPasses = (ArrayList<TollPass>) repository.getAllTollPasses();

        //the final Array List with all the costs for the given user
        ArrayList<Double> allPayments = new ArrayList<>();

        for (TollPass toll : tollPasses){

            //if the toll pass is between the 2 dates AND is not sucive
            if ( toll.getPassDate().isAfter(from)
                    && toll.getPassDate().isBefore(to)
                    && toll.getPaymentType() != PaymentTypeData.SUCIVE){

                    allPayments.add(toll.getCost());
            }
        }

        if (allPayments.isEmpty()){
            return Optional.empty();
        }else{
            return Optional.of(allPayments);
        }
    }

    //This functions means that I CARE about what users are registering to other modules
    //This functions means that I CARE about what users are registering to other modules
    //This functions means that I CARE about what users are registering to other modules
    //This functions means that I CARE about what users are registering to other modules
    //This functions means that I CARE about what users are registering to other modules
    //This functions means that I CARE about what users are registering to other modules
    @Override
    public Optional<List<Double>> paymentInquiry(UserDTO userDTO) throws NoCustomerException {

        //check for the customer to be properly initialized
        if (userDTO.getTollCustomer() == null){
            throw new NoCustomerException("El cliente "+ userDTO.getName() +" no está registrado a Telepeaje");
        }

        //I get my user from the DB
        User user = repository.getUserById(userDTO.getId());

        //I get all the links from the user
        ArrayList<Link> userLinks = (ArrayList<Link>) user.getLinkedCars();

        //the final Array List with all of the costs for the given user
        ArrayList<Double> allPayments = new ArrayList<>();

        //for each link in user, I get the vehicle, and from the vehicle I get the TollPasses
        for (Link link : userLinks) {

            //I get the vehicle
            Vehicle userVehicle = link.getVehicle();

            //I get the toll passes from the vehicle
            ArrayList<TollPass> tollPassArrayList = (ArrayList<TollPass>) userVehicle.getTollPass();

            //for each toll pass, I add it's cost to the payment array
            for (TollPass toll : tollPassArrayList) {
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
    public Optional<List<Double>> paymentInquiry(UserDTO userDTO,
                                                 VehicleDTO vehicleDTO) throws NoCustomerException, InvalidVehicleException {

//        //I get all the links from the user
//        ArrayList<LinkDTO> userLinks = (ArrayList<LinkDTO>) user.getLinkedVehicles();
//
//        //the final Array List with all of the costs for the given user
//        ArrayList<Double> allPayments = new ArrayList<>();
//
//        for (LinkDTO link : userLinks){
//            VehicleDTO userVehicleDTO  = link.getVehicle();
//
//            if (userVehicleDTO.equals(vehicle)){
//                ArrayList<TollPassDTO> tollPassDTOArrayList = (ArrayList<TollPassDTO>) userVehicleDTO.getTollPassDTO();
//
//                for(TollPassDTO toll : tollPassDTOArrayList) {
//
//                    if (toll.getPaymentType() != PaymentTypeData.SUCIVE){//again, I only get toll payments from pre & post pagos
//
//                        allPayments.add(toll.getCost());
//                    }
//                }
//                return Optional.of(allPayments);
//            }
//        }
//        return Optional.empty();
//    }//this code it's so nested, it makes me wish for WW3. . .

        //check for the customer to be properly initialized
        if (userDTO.getTollCustomer() == null) {
            throw new NoCustomerException("El cliente " + userDTO.getName() + " no está registrado a Telepeaje");
        }

        //I get my user from the DB
        User user = repository.getUserById(userDTO.getId());

        //I get all the links from the user
        ArrayList<Link> userLinks = (ArrayList<Link>) user.getLinkedCars();

        //the final Array List with all of the costs for the given user
        ArrayList<Double> allPayments = new ArrayList<>();

        //for each link in user, I get the vehicle, and from the vehicle I get the TollPasses
        for (Link link : userLinks) {

            //I get the vehicle
            Vehicle userVehicle = link.getVehicle();

            //this looks scary. . .
            if(userVehicle.getId() == vehicleDTO.getId()){
                
                //I get the toll passes from the vehicle
                ArrayList<TollPass> tollPassArrayList = (ArrayList<TollPass>) userVehicle.getTollPass();

                //for each toll pass, I add it's cost to the payment array
                for (TollPass toll : tollPassArrayList) {
                    allPayments.add(toll.getCost());
                }
            }
        }

        if (allPayments.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(allPayments);
        }

    }
}
