package com.traffic.payment.Interface.impl;

import com.traffic.dtos.account.CreditCardDTO;
import com.traffic.dtos.account.PostPayDTO;
import com.traffic.dtos.account.PrePayDTO;
import com.traffic.dtos.user.TollCustomerDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.dtos.vehicle.*;
import com.traffic.exceptions.ExternalApiException;
import com.traffic.exceptions.InternalErrorException;
import com.traffic.exceptions.InvalidVehicleException;
import com.traffic.exceptions.NoCustomerException;
import com.traffic.payment.Interface.PaymentController;
import com.traffic.payment.domain.account.CreditCard;
import com.traffic.payment.domain.account.PostPay;
import com.traffic.payment.domain.account.PrePay;
import com.traffic.payment.domain.repository.PaymentRepository;
import com.traffic.payment.domain.user.TollCustomer;
import com.traffic.payment.domain.user.User;
import com.traffic.payment.domain.vehicle.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PaymentControllerImpl implements PaymentController {

    //I inject the repository which "represents" the DB we're supposedly working with
    @Inject
    private PaymentRepository repository;


    @Override
    public void customerRegistration(UserDTO user,
                                     CreditCardDTO creditCard) throws ExternalApiException, NoCustomerException, InternalErrorException {

        //check for the customer to be properly initialized
        if (user.getTollCustomer() == null){
            throw new NoCustomerException("El cliente no está registrado a Telepeaje");
        }
        if (creditCard.getExpireDate().isBefore(LocalDate.now())){
            throw new InternalErrorException("La tarjeta de crédito está vencida");
        }

        //another cute thing TODO would be to move my DTO to Domain object functions to my Domain classes

        //preparo los DTOs para ser pasados a objetos de Dominio
        // creditCard > pre & postPay > tollCustomer
        TollCustomerDTO tollCustomerDTO = user.getTollCustomer();
        PostPayDTO postPayDTO = tollCustomerDTO.getPostPayDTO();
        PrePayDTO prePayDTO = tollCustomerDTO.getPrePayDTO();

        CreditCard card = new CreditCard(creditCard.getCardNumber(),creditCard.getName(),creditCard.getExpireDate());

        //postPayDTO to postPay
        PostPay postPay =  new PostPay(postPayDTO.getAccountNumber(),postPayDTO.getCreationDate(), card );

        //prePayDTO to prePay
        PrePay prePay = new PrePay(prePayDTO.getAccountNumber(),prePayDTO.getCreationDate(),prePayDTO.getBalance());

        //to prepare tollCustomer, I need postPay & prePay
        TollCustomer tollCustomer = new TollCustomer(postPay, prePay);

        //list of LinkDTOs from the user
        List <LinkDTO> vehicleLinksDTOs = user.getLinkedVehicles();

        ArrayList<Link> vehicleLinks = new ArrayList<>();

        for (LinkDTO link  : vehicleLinksDTOs){

            ArrayList<TollPass> passes = new ArrayList<>(); //Domain Object list of all TollPases of a given vehicle

            if (link.getVehicle() instanceof ForeignVehicleDTO){

                ForeignVehicleDTO vehicleDTO = (ForeignVehicleDTO) link.getVehicle();

                //I get the list of toll passes DTOs for the vehicle of the given Link
                ArrayList<TollPassDTO> tollPassListToIterate = (ArrayList<TollPassDTO>) vehicleDTO.getTollPassDTO();

                //Iterate through it to get the Domain object list of toll pases for the given vehicle
                for (TollPassDTO toll : tollPassListToIterate){

                    TollPass tollToAdd = new TollPass(toll.getDate(),toll.getCost(),toll.getPaymentType());
                    passes.add(tollToAdd);
                }
                //transform TagDTO to Tag
                Tag tagToAdd = new Tag(vehicleDTO.getId());
                ForeignVehicle vehicleToAdd = new ForeignVehicle(vehicleDTO.getId(),passes,tagToAdd);

                //I need a vehicle domain object in order to create my link
                Link linkToAdd = new Link(link.getId(), link.getInitialDate(), link.getActive(), vehicleToAdd);

                vehicleLinks.add(linkToAdd);

            }else if (link.getVehicle() instanceof NationalVehicleDTO){ //we do the same but for NationalVehicle

                NationalVehicleDTO vehicleDTO = (NationalVehicleDTO) link.getVehicle();

                //I get the list of toll passes DTOs for the vehicle of the given Link
                ArrayList<TollPassDTO> tollPassListToIterate = (ArrayList<TollPassDTO>) vehicleDTO.getTollPassDTO();

                //Iterate through it to get the Domain object list of toll pases for the given vehicle
                for (TollPassDTO toll : tollPassListToIterate){

                    TollPass tollToAdd = new TollPass( toll.getDate(),toll.getCost(),toll.getPaymentType());
                    passes.add(tollToAdd);
                }

                //transform TagDTO to Tag
                Tag tagToAdd = new Tag(vehicleDTO.getId());

                LicensePlate licenseToAdd = new LicensePlate(  vehicleDTO.getLicensePlateDTO().getId(), vehicleDTO.getLicensePlateDTO().getLicensePlateNumber() );
                NationalVehicle vehicleToAdd = new NationalVehicle(vehicleDTO.getId(), passes, tagToAdd, licenseToAdd);

                //I need a vehicle domain object in order to create my link
                Link linkToAdd = new Link(link.getId(), link.getInitialDate(), link.getActive(), vehicleToAdd);

                vehicleLinks.add(linkToAdd);
            }else{
                throw new InternalErrorException("No hay ningún vehiculo registrado para el usuario: " + user.getName());
            }
        }


        User userToAdd = new User(user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getName(),
                user.getCi(),
                tollCustomer,
                vehicleLinks
                );

        //we persist the data (currently using memory implementation)
        repository.addUser(userToAdd);
    }

    @Override
    public void notifyPayment(UserDTO user,
                              VehicleDTO vehicle,
                              Double amount,
                              CreditCardDTO creditCard) throws ExternalApiException, NoCustomerException, IllegalArgumentException {

        //check for the customer to be properly initialized
        if (user.getTollCustomer() == null){
            throw new NoCustomerException("El cliente no está registrado a Telepeaje");
        }
        if (creditCard.getExpireDate().isBefore(LocalDate.now())){
            throw new IllegalArgumentException("La tarjeta de crédito está vencida");
        }





    }

    @Override
    public Optional<List<Double>> paymentInquiry(LocalDate from, LocalDate to) {
        //we get all the users from the repository
        ArrayList<User> users = repository.getAllUsers();

        //the final Array List with all the costs for the given user
        ArrayList<Double> allPayments = new ArrayList<>();

        for (User user : users){
            //I get all the links from the user
            ArrayList<Link> userLinks = (ArrayList<Link>) user.getLinkedVehicles();

            for (Link link : userLinks){
                Vehicle vehicle = link.getVehicle(); //I get the vehicle from the link
                ArrayList<TollPass> tollPasses = (ArrayList<TollPass>) vehicle.getTollPass(); //I get all the Toll passes from the vehicle

                for (TollPass toll : tollPasses){

                    if ( toll.getDate().isAfter(from) && toll.getDate().isBefore(to)){ //if the toll pass is between the 2 dates
                        allPayments.add(toll.getCost());
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

    @Override
    public Optional<List<Double>> paymentInquiry(UserDTO user) throws NoCustomerException { //TODO check all the potential controls are properly implemented
        //check for the customer to be properly initialized
        if (user.getTollCustomer() == null){
            throw new NoCustomerException("El cliente no está registrado a Telepeaje");
        }

        //I get all the links from the user
        ArrayList<LinkDTO> userLinks = (ArrayList<LinkDTO>) user.getLinkedVehicles();

        //the final Array List with all of the costs for the given user
        ArrayList<Double> allPayments = new ArrayList<>();

        for (LinkDTO link : userLinks){ //for each link in user, I get the vehicle, and from the vehicle I get the TollPasses
            VehicleDTO userVehicleDTO  = link.getVehicle();
            ArrayList<TollPassDTO> tollPassDTOArrayList = (ArrayList<TollPassDTO>) userVehicleDTO.getTollPassDTO();

            for(TollPassDTO toll : tollPassDTOArrayList) {
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
    public Optional<List<Double>> paymentInquiry(UserDTO user,
                                                 VehicleDTO vehicle) throws NoCustomerException, InvalidVehicleException {

        //check for the customer to be properly initialized
        if (user.getTollCustomer() == null){
            throw new NoCustomerException("El cliente no está registrado a Telepeaje");
        }
        if (vehicle == null){
            throw new InvalidVehicleException("El vehiculo no está registrado");
        }

        //I get all the links from the user
        ArrayList<LinkDTO> userLinks = (ArrayList<LinkDTO>) user.getLinkedVehicles();

        //the final Array List with all of the costs for the given user
        ArrayList<Double> allPayments = new ArrayList<>();

        for (LinkDTO link : userLinks){
            VehicleDTO userVehicleDTO  = link.getVehicle();

            if (userVehicleDTO.equals(vehicle)){
                ArrayList<TollPassDTO> tollPassDTOArrayList = (ArrayList<TollPassDTO>) userVehicleDTO.getTollPassDTO();

                for(TollPassDTO toll : tollPassDTOArrayList) {
                    allPayments.add(toll.getCost());
                }
                return Optional.of(allPayments);
            }
        }

        return Optional.empty();
    }
}
