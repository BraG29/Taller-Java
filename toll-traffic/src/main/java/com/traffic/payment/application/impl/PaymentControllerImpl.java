package com.traffic.payment.application.impl;

import com.traffic.dtos.account.CreditCardDTO;
import com.traffic.dtos.account.PostPayDTO;
import com.traffic.dtos.account.PrePayDTO;
import com.traffic.dtos.user.TollCustomerDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.dtos.vehicle.*;
import com.traffic.exceptions.ExternalApiException;
import com.traffic.exceptions.InvalidVehicleException;
import com.traffic.exceptions.NoCustomerException;
import com.traffic.payment.application.PaymentController;
import com.traffic.payment.domain.account.Account;
import com.traffic.payment.domain.account.CreditCard;
import com.traffic.payment.domain.account.PostPay;
import com.traffic.payment.domain.account.PrePay;
import com.traffic.payment.domain.repository.PaymentRepositoryImplementation;
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
    private PaymentRepositoryImplementation repository;


    @Override
    public void customerRegistration(UserDTO user,
                                     CreditCardDTO creditCard) throws ExternalApiException, NoCustomerException {

        //check for the customer to be properly initialized
        if (user.getTollCustomer() == null){
            throw new NoCustomerException("El cliente no está registrado a Telepeaje");
        }

        // TODO implement the control to see if I am receiving a proper creditCard or not
        //another cute thing TODO would be to move my DTO to Domain object functions to my Domain classes

        //preparo los DTOs para ser pasados a objetos de Dominio
        // creditCard > pre & postPay > tollCustomer
        TollCustomerDTO tollCustomerDTO = user.getTollCustomer();
        PostPayDTO postPayDTO = tollCustomerDTO.getPostPayDTO();
        PrePayDTO prePayDTO = tollCustomerDTO.getPrePayDTO();

        //there should be a control here to check if the creditCard does not come expired right?
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

                    TollPass tollToAdd = new TollPass(toll.getId(), toll.getDate(),toll.getCost(),toll.getPaymentType());
                    passes.add(tollToAdd);
                }
                //transform TagDTO to Tag
                Tag tagToAdd = new Tag(vehicleDTO.getId());
                ForeignVehicle vehicleToAdd = new ForeignVehicle(vehicleDTO.getId(),passes,tagToAdd);

                //I need a vehicle domain object in order to create my link
                Link linkToAdd = new Link(link.getId(), link.getInitialDate(), link.getActive(), vehicleToAdd);

                vehicleLinks.add(linkToAdd);

            }else{ //we do the same but for NationalVehicle

                NationalVehicleDTO vehicleDTO = (NationalVehicleDTO) link.getVehicle();

                //I get the list of toll passes DTOs for the vehicle of the given Link
                ArrayList<TollPassDTO> tollPassListToIterate = (ArrayList<TollPassDTO>) vehicleDTO.getTollPassDTO();

                //Iterate through it to get the Domain object list of toll pases for the given vehicle
                for (TollPassDTO toll : tollPassListToIterate){

                    TollPass tollToAdd = new TollPass(toll.getId(), toll.getDate(),toll.getCost(),toll.getPaymentType());
                    passes.add(tollToAdd);
                }

                //transform TagDTO to Tag
                Tag tagToAdd = new Tag(vehicleDTO.getId());

                LicensePlate licenseToAdd = new LicensePlate(  vehicleDTO.getLicensePlateDTO().getId(), vehicleDTO.getLicensePlateDTO().getLicensePlateNumber() );
                NationalVehicle vehicleToAdd = new NationalVehicle(vehicleDTO.getId(), passes, tagToAdd, licenseToAdd);

                //I need a vehicle domain object in order to create my link
                Link linkToAdd = new Link(link.getId(), link.getInitialDate(), link.getActive(), vehicleToAdd);

                vehicleLinks.add(linkToAdd);
            }
            //TODO control para si el vehiculo no es ni nacional ni foráneo (oséase NULL)
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
        //TODO surround with try and catch for the external API exception
        repository.addUser(userToAdd);


    }

    @Override
    public void notifyPayment(UserDTO user,
                              VehicleDTO vehicle,
                              Double amount,
                              CreditCardDTO creditCard) throws ExternalApiException, NoCustomerException, IllegalArgumentException {

    }

    @Override
    public Optional<List<Double>> paymentInquiry(LocalDate from, LocalDate to) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Double>> paymentInquiry(UserDTO user) throws NoCustomerException {
        return Optional.empty();
    }

    @Override
    public Optional<List<Double>> paymentInquiry(UserDTO user,
                                                 VehicleDTO vehicle) throws NoCustomerException, InvalidVehicleException {
        return Optional.empty();
    }
}
