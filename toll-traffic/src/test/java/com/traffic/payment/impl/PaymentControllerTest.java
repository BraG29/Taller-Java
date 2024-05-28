//package com.traffic.payment.impl;
//
//import com.traffic.dtos.PaymentTypeData;
//import com.traffic.dtos.account.CreditCardDTO;
//import com.traffic.dtos.account.PostPayDTO;
//import com.traffic.dtos.account.PrePayDTO;
//import com.traffic.dtos.user.NationalUserDTO;
//import com.traffic.dtos.user.TollCustomerDTO;
//import com.traffic.dtos.user.UserDTO;
//import com.traffic.dtos.vehicle.*;
//import com.traffic.exceptions.ExternalApiException;
//import com.traffic.exceptions.InternalErrorException;
//import com.traffic.exceptions.NoCustomerException;
//import com.traffic.payment.Interface.PaymentController;
//import com.traffic.payment.Interface.impl.PaymentControllerImpl;
//import com.traffic.payment.domain.repository.PaymentRepositoryImplementation;
//import jakarta.inject.Inject;
//import org.jboss.weld.junit5.auto.AddPackages;
//import org.jboss.weld.junit5.auto.EnableAutoWeld;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//@EnableAutoWeld
//@AddPackages(PaymentControllerImpl.class)
//@AddPackages(PaymentRepositoryImplementation.class)
//public class PaymentControllerTest {
//    /*
//    these tests are for the functionalities provided by the payment module, entirely local, and for now
//    mockito less
//     */
//    @Inject
//    private PaymentController paymentController;
//
//    @Test
//    public void testCostumerRegistrationOK() throws ExternalApiException, NoCustomerException, InternalErrorException {
//
//        CreditCardDTO creditCardDTO = new CreditCardDTO(0L, "1234567898765", "VISA", LocalDate.of(2025, 12, 12));
//        UserDTO user = generateCorrectNationalUser();
//        try {
//            paymentController.customerRegistration(user, creditCardDTO);
//        } catch (Exception e) {
//            //acá estoy generalizado los 3 posibles tipos de excepciones que podrían llegar a ser tiradas
//            System.out.println(e.getMessage());
//            throw e;
//        }
//    }
//
//    @Test
//    public void testCostumerRegistrationERROR() throws ExternalApiException, NoCustomerException, InternalErrorException {
//        CreditCardDTO creditCardDTO = new CreditCardDTO(0L, "1234567898765", "VISA", LocalDate.of(2020, 12, 12));
//        UserDTO user = generateCorrectNationalUser();
//
//        try {
//            paymentController.customerRegistration(user, creditCardDTO);
//        } catch (Exception e) {
//            //acá estoy generalizado los 2 posibles tipos de excepciones que podrían llegar a ser tiradas
//            System.out.println(e.getMessage());
//            throw e;
//        }
//    }
//
//
//    public NationalUserDTO generateCorrectNationalUser() {
//        //valores que me importan: usuario y credito, lo demás son datos que precisan los DTOs para ser construidos
//        //y que yo, tontamente, implementé tal cual en mis objetos de dominio
//
//        //loading of Credit Card, PrePay & PostPay DTOs
//        CreditCardDTO creditCardDTO = new CreditCardDTO(0L, "1234567898765", "VISA", LocalDate.now());
//        PostPayDTO postPayDTO = new PostPayDTO(0L, 123, LocalDate.now(), creditCardDTO);
//        PrePayDTO prePayDTO = new PrePayDTO(0L, 123, LocalDate.now(), 200D);
//
//        //I create 3 tollPasses for a given toll Customer
//        TollCustomerDTO tollCustomer = new TollCustomerDTO(0L, postPayDTO, prePayDTO);
//        TollPassDTO tollPass1 = new TollPassDTO(LocalDate.now(), 500D, PaymentTypeData.PRE_PAYMENT);
//        TollPassDTO tollPass2 = new TollPassDTO(LocalDate.now(), 500D, PaymentTypeData.POST_PAYMENT);
//        TollPassDTO tollPass3 = new TollPassDTO(LocalDate.now(), 1000D, PaymentTypeData.SUCIVE);
//
//        List<TollPassDTO> tollPassDTOList = new ArrayList<>();
//        tollPassDTOList.add(tollPass1);
//        tollPassDTOList.add(tollPass2);
//        tollPassDTOList.add(tollPass3);
//
//        //we create a tag & license for a given National Vehicle, alongside adding the previous toll Pass list
//        TagDTO tagDTO1 = new TagDTO(0L);
//        LicensePlateDTO licensePlateDTO = new LicensePlateDTO(0L, "1776437");
//        NationalVehicleDTO nationalVehicle = new NationalVehicleDTO(0L, tollPassDTOList, tagDTO1, licensePlateDTO);
//
//        //we create a tag for a given Foreign Vehicle alongside adding the previous toll Pass list
//        TagDTO tagDTO2 = new TagDTO(1L);
//        ForeignVehicleDTO foreignVehicle = new ForeignVehicleDTO(0L, tollPassDTOList, tagDTO2);
//
//        //we add the vehicles to our user's Links
//        List<LinkDTO> linkDTOList = new ArrayList<>();
//        LinkDTO link1 = new LinkDTO(0L, LocalDate.of(2025, 12, 12), true, nationalVehicle);
//        LinkDTO link2 = new LinkDTO(1L, LocalDate.of(2025, 12, 12), true, foreignVehicle);
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
//        return user;
//    }
//}
