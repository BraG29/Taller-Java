package com.traffic.sucive.Interface.impl;

import com.traffic.sucive.Interface.SuciveController;
import com.traffic.sucive.domain.repository.SuciveRepository;
import com.traffic.sucive.domain.repository.SuciveRepositoryImplementation;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@EnableAutoWeld
@AddPackages(SuciveControllerImpl.class)
@AddPackages(SuciveRepositoryImplementation.class)
public class SuciveControllerTest {
    /*
these tests are for the functionalities provided by the Sucive module, entirely local, and for now
mockito less
 */

    @Inject
    private SuciveControllerImpl controller;

    @Inject
    private SuciveRepositoryImplementation repository;

//    @Inject
//    private SuciveRepository repository;

    @Test
    public void paymentInquiryDateOKTest(){
        //NationalUserDTO userDTO = generateCorrectNationalUserDTO();


//        ArrayList<Link> links = new ArrayList<>();
//        TollCustomer tollCustomer = new TollCustomer();

//        for (LinkDTO linkDTO : userDTO.getLinkedVehicles()){
//            linkDTO.get
//        }

//    //I get the vehicleLinksDTOs from the user I generated
//    List <LinkDTO> vehicleLinksDTOs = userDTO.getLinkedVehicles();
//
//    //I create the array that will hold the vehicleLinks of the new Domain Object User
//    ArrayList<Link> vehicleLinks = new ArrayList<>();
//
//    //I iterate through all the vehicleLinks from the DTO
//    for (LinkDTO link  : vehicleLinksDTOs){
//
//        //Domain Object list of all TollPases of a given vehicle
//        ArrayList<TollPass> passes = new ArrayList<>();
//
//        if (link.getVehicle() instanceof ForeignVehicleDTO){
//
//            ForeignVehicleDTO vehicleDTO = (ForeignVehicleDTO) link.getVehicle();
//
//            //I get the list of toll passes DTOs for the vehicle of the given Link
//            ArrayList<TollPassDTO> tollPassListToIterate = (ArrayList<TollPassDTO>) vehicleDTO.getTollPassDTO();
//
//            //Iterate through it to get the Domain object list of toll pases for the given vehicle
//            for (TollPassDTO toll : tollPassListToIterate){
//
//                TollPass tollToAdd = new TollPass(toll.getDate(),toll.getCost(),toll.getPaymentType());
//                passes.add(tollToAdd);
//            }
//            //transform TagDTO to Tag
//            Tag tagToAdd = new Tag(vehicleDTO.getId());
//            ForeignVehicle vehicleToAdd = new ForeignVehicle(vehicleDTO.getId(),passes,tagToAdd);
//
//            //I need a vehicle domain object in order to create my link
//            com.traffic.payment.domain.vehicle.Link linkToAdd = new com.traffic.payment.domain.vehicle.Link(link.getId(), link.getInitialDate(), link.getActive(), vehicleToAdd);
//
//            vehicleLinks.add(linkToAdd);
//
//        }else if (link.getVehicle() instanceof NationalVehicleDTO){ //we do the same but for NationalVehicle
//
//            NationalVehicleDTO vehicleDTO = (NationalVehicleDTO) link.getVehicle();
//
//            //I get the list of toll passes DTOs for the vehicle of the given Link
//            ArrayList<TollPassDTO> tollPassListToIterate = (ArrayList<TollPassDTO>) vehicleDTO.getTollPassDTO();
//
//            //Iterate through it to get the Domain object list of toll pases for the given vehicle
//            for (TollPassDTO toll : tollPassListToIterate){
//
//                TollPass tollToAdd = new TollPass( toll.getDate(),toll.getCost(),toll.getPaymentType());
//                passes.add(tollToAdd);
//            }
//
//            //transform TagDTO to Tag
//            Tag tagToAdd = new Tag(vehicleDTO.getId());
//
//            LicensePlate licenseToAdd = new LicensePlate(  vehicleDTO.getLicensePlateDTO().getId(), vehicleDTO.getLicensePlateDTO().getLicensePlateNumber() );
//            NationalVehicle vehicleToAdd = new NationalVehicle(vehicleDTO.getId(), passes, tagToAdd, licenseToAdd);
//
//            //I need a vehicle domain object in order to create my link
//            com.traffic.payment.domain.vehicle.Link linkToAdd = new com.traffic.payment.domain.vehicle.Link(link.getId(), link.getInitialDate(), link.getActive(), vehicleToAdd);
//
//            vehicleLinks.add(linkToAdd);
//        }else{
//            throw new InternalErrorException("No hay ningún vehiculo registrado para el usuario: " + user.getName());
//        }
//    }
//
//        User user = new User(userDTO.getId(), userDTO.getEmail(), userDTO.getPassword(), userDTO.getName(), userDTO.getCi(), userDTO.getTollCustomer(), userDTO.getLinkedVehicles());



    }



//
//    public NationalUserDTO generateCorrectNationalUserDTO(){
//        //I re used this function from the test of payment module, as I need to have an user to test my sucive functionalities
//
//        //loading of Credit Card, PrePay & PostPay DTOs
//        CreditCardDTO creditCardDTO = new CreditCardDTO(0L,"1234567898765","VISA", LocalDate.now());
//        PostPayDTO postPayDTO = new PostPayDTO(0L,123, LocalDate.now(),creditCardDTO);
//        PrePayDTO prePayDTO = new PrePayDTO(0L,123,LocalDate.now(),200D);
//
//        //I create a toll customer, that I will be filling with info soon enough
//        TollCustomerDTO tollCustomer = new TollCustomerDTO(0L,postPayDTO,prePayDTO);
//
//        ////I create 3 tollPasses for the given toll Customer
//        TollPassDTO tollPassNationalVehicle1 = new TollPassDTO(LocalDate.now(),250D, PaymentTypeData.PRE_PAYMENT);
//        TollPassDTO tollPassNationalVehicle2 = new TollPassDTO(LocalDate.now(),500D,PaymentTypeData.POST_PAYMENT);
//        TollPassDTO tollPassNationalVehicle3 = new TollPassDTO(LocalDate.now(),1000D,PaymentTypeData.SUCIVE);
//
//        //the list  that will hold said toll passes
//        List<TollPassDTO> tollPassDTOListNationalVehicle = new ArrayList<>();
//
//        //I assign the tollPasses to a list which I will later add to a National vehicle
//        tollPassDTOListNationalVehicle.add(tollPassNationalVehicle1);
//        tollPassDTOListNationalVehicle.add(tollPassNationalVehicle2);
//        tollPassDTOListNationalVehicle.add(tollPassNationalVehicle3);
//
//        //we create a tag & license for a given National Vehicle, alongside adding the previous toll Pass list to it
//        TagDTO tagDTO1 = new TagDTO(0L);
//        LicensePlateDTO licensePlateDTO = new LicensePlateDTO(0L,"1776437");
//        NationalVehicleDTO nationalVehicle = new NationalVehicleDTO(0L,tollPassDTOListNationalVehicle,tagDTO1,licensePlateDTO);
//
//
//        //now we create the Toll passes for a FOREIGN vehicle
//        TollPassDTO tollPassForeignVehicle1 = new TollPassDTO(LocalDate.of(2030,1,1),100D,PaymentTypeData.PRE_PAYMENT);
//        TollPassDTO tollPassForeignVehicle2 = new TollPassDTO(LocalDate.now(),300D,PaymentTypeData.POST_PAYMENT);
//        TollPassDTO tollPassForeignVehicle3 = new TollPassDTO(LocalDate.of(2000,1,1),600D,PaymentTypeData.SUCIVE);
//
//        //we add those toll passes to a list
//        List<TollPassDTO> tollPassDTOListForeignVehicle = new ArrayList<>();
//
//        tollPassDTOListForeignVehicle.add(tollPassForeignVehicle1);
//        tollPassDTOListForeignVehicle.add(tollPassForeignVehicle2);
//        tollPassDTOListForeignVehicle.add(tollPassForeignVehicle3);
//
//        //we create a tag for a given Foreign Vehicle alongside adding the previous toll Pass list
//        TagDTO tagDTO2 = new TagDTO(1L);
//        ForeignVehicleDTO foreignVehicle = new ForeignVehicleDTO(0L,tollPassDTOListForeignVehicle,tagDTO2);
//
//        //we add the vehicles to our user's Links
//        List<LinkDTO> linkDTOList = new ArrayList<>();
//        LinkDTO link1 = new LinkDTO(0L,LocalDate.of(2025,12,12),true,nationalVehicle);
//        LinkDTO link2 = new LinkDTO(1L,LocalDate.of(2030,1,1),true,foreignVehicle);
//        linkDTOList.add(link1);
//        linkDTOList.add(link2);
//
//        NationalUserDTO user = new NationalUserDTO(0L,
//                "email@mail.com",
//                "1234",
//                "jose",
//                "53349029",
//                tollCustomer,
//                linkDTOList,
//                null,
//                null);
//
//
//
//        return user;
//    }


    @Test
    public void testPostConstructPaymentInquiry(){

        Optional<List<Double>> payments = Optional.empty();
        payments = controller.paymentInquiry(LocalDate.now(), LocalDate.of(2030,1,1));
        System.out.println("Estoy probando Payment Inquiry de Sucive-----------------------------------------------------------------------------------------------------");
        System.out.println(payments.toString());
    }
}
