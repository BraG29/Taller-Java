package com.traffic.payment.impl;

import com.traffic.client.domain.User.NationalUser;
import com.traffic.dtos.PaymentTypeData;
import com.traffic.dtos.account.CreditCardDTO;
import com.traffic.dtos.account.PostPayDTO;
import com.traffic.dtos.account.PrePayDTO;
import com.traffic.dtos.user.NationalUserDTO;
import com.traffic.dtos.user.TollCustomerDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.dtos.vehicle.*;
import com.traffic.exceptions.ExternalApiException;
import com.traffic.exceptions.InternalErrorException;
import com.traffic.exceptions.InvalidVehicleException;
import com.traffic.exceptions.NoCustomerException;
import com.traffic.payment.Interface.PaymentController;
import com.traffic.payment.domain.repository.PaymentRepository;
import jakarta.inject.Inject;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@EnableAutoWeld
@AddPackages(PaymentController.class)
@AddPackages(PaymentRepository.class)
public class PaymentControllerTest {
/*
these tests are for the functionalities provided by the payment module, entirely local, and for now
mockito less
 */
@Inject
    private PaymentController paymentController;
@Inject
    private PaymentRepository repository;

    @Test
    public void testCostumerRegistrationOK() throws ExternalApiException, NoCustomerException, InternalErrorException {

        CreditCardDTO creditCardDTO = new CreditCardDTO(0L,"1234567898765","VISA",LocalDate.of(2025,12,12));
        UserDTO user = generateCorrectNationalUser();
        try {
            paymentController.customerRegistration(user,creditCardDTO);
        } catch (Exception e) {
            //acá estoy generalizado los 3 posibles tipos de excepciones que podrían llegar a ser tiradas
            System.out.println(e.getMessage());
            throw e;
        }
    }



    @Test
    public void testCostumerRegistrationERROR() throws ExternalApiException, NoCustomerException, InternalErrorException {
        CreditCardDTO creditCardDTO = new CreditCardDTO(0L,"1234567898765","VISA",LocalDate.of(2020,12,12));
        UserDTO user = generateCorrectNationalUser();

        Assertions.assertThrows(InternalErrorException.class, () ->
                paymentController.customerRegistration(user,creditCardDTO)
                );
    }


    public NationalUserDTO generateCorrectNationalUser(){
        //valores que me importan: usuario y credito, lo demás son datos que precisan los DTOs para ser construidos
        //y que yo, tontamente, implementé tal cual en mis objetos de dominio

        //loading of Credit Card, PrePay & PostPay DTOs
        CreditCardDTO creditCardDTO = new CreditCardDTO(0L,"1234567898765","VISA",LocalDate.now());
        PostPayDTO postPayDTO = new PostPayDTO(0L,123, LocalDate.now(),creditCardDTO);
        PrePayDTO prePayDTO = new PrePayDTO(0L,123,LocalDate.now(),200D);

        //I create 3 tollPasses for a given toll Customer
        TollCustomerDTO tollCustomer = new TollCustomerDTO(0L,postPayDTO,prePayDTO);

        TollPassDTO tollPassNationalVehicle1 = new TollPassDTO(LocalDate.now(),250D,PaymentTypeData.PRE_PAYMENT);
        TollPassDTO tollPassNationalVehicle2 = new TollPassDTO(LocalDate.now(),500D,PaymentTypeData.POST_PAYMENT);
        TollPassDTO tollPassNationalVehicle3 = new TollPassDTO(LocalDate.now(),1000D,PaymentTypeData.SUCIVE);

        List<TollPassDTO> tollPassDTOListNationalVehicle = new ArrayList<>();

        //I assign the tollPasses to a list which I will later add to a National vehicle
        tollPassDTOListNationalVehicle.add(tollPassNationalVehicle1);
        tollPassDTOListNationalVehicle.add(tollPassNationalVehicle2);
        tollPassDTOListNationalVehicle.add(tollPassNationalVehicle3);

        //we create a tag & license for a given National Vehicle, alongside adding the previous toll Pass list
        TagDTO tagDTO1 = new TagDTO(0L);
        LicensePlateDTO licensePlateDTO = new LicensePlateDTO(0L,"1776437");
        NationalVehicleDTO nationalVehicle = new NationalVehicleDTO(0L,tollPassDTOListNationalVehicle,tagDTO1,licensePlateDTO);



        TollPassDTO tollPassForeignVehicle1 = new TollPassDTO(LocalDate.of(2030,1,1),100D,PaymentTypeData.PRE_PAYMENT);
        TollPassDTO tollPassForeignVehicle2 = new TollPassDTO(LocalDate.now(),300D,PaymentTypeData.POST_PAYMENT);
        TollPassDTO tollPassForeignVehicle3 = new TollPassDTO(LocalDate.of(2000,1,1),600D,PaymentTypeData.SUCIVE);

        List<TollPassDTO> tollPassDTOListForeignVehicle = new ArrayList<>();

        //I assign the tollPasses to a list which I will later add to a National vehicle
        tollPassDTOListForeignVehicle.add(tollPassForeignVehicle1);
        tollPassDTOListForeignVehicle.add(tollPassForeignVehicle2);
        tollPassDTOListForeignVehicle.add(tollPassForeignVehicle3);

        //we create a tag for a given Foreign Vehicle alongside adding the previous toll Pass list
        TagDTO tagDTO2 = new TagDTO(1L);
        ForeignVehicleDTO foreignVehicle = new ForeignVehicleDTO(0L,tollPassDTOListForeignVehicle,tagDTO2);

        //we add the vehicles to our user's Links
        List<LinkDTO> linkDTOList = new ArrayList<>();
        LinkDTO link1 = new LinkDTO(0L,LocalDate.of(2025,12,12),true,nationalVehicle);
        LinkDTO link2 = new LinkDTO(1L,LocalDate.of(2030,1,1),true,foreignVehicle);
        linkDTOList.add(link1);
        linkDTOList.add(link2);

        NationalUserDTO user = new NationalUserDTO(0L,
                "email@mail.com",
                "1234",
                "jose",
                "53349029",
                tollCustomer,
                linkDTOList,
                null,
                null);
        return user;
    }


    @Test
    public void paymentInquiryUserAndVehicleTestOk() throws NoCustomerException, InvalidVehicleException {
        NationalUserDTO nationalUser = generateCorrectNationalUser();

        NationalVehicleDTO nationalVehicle = (NationalVehicleDTO) nationalUser.getLinkedVehicles().get(0).getVehicle();
        ForeignVehicleDTO foreignVehicle = (ForeignVehicleDTO) nationalUser.getLinkedVehicles().get(1).getVehicle();

        System.out.println("Valores del vehiculo nacional del usuario");
        Optional<List<Double>> paymentListNational = paymentController.paymentInquiry(nationalUser,nationalVehicle);
        System.out.println(paymentListNational.toString());

        System.out.println("Valores del vehiculo foráneo del usuario");
        Optional<List<Double>> paymentListForeign = paymentController.paymentInquiry(nationalUser,foreignVehicle);
        System.out.println(paymentListForeign.toString());
    }

    @Test
    public void paymentInquiryUserTestOk() throws NoCustomerException{
        NationalUserDTO nationalUser = generateCorrectNationalUser();

        Optional<List<Double>> paymentList = paymentController.paymentInquiry(nationalUser);
        System.out.println(paymentList.toString());//it prints all the prices from the toll passes of both, national and foreign vehicles the user has registered
        //it ignores all SUCIVE payments
    }

    @Test
    public void paymentInquiryDateTestOk() throws NoCustomerException, ExternalApiException, InternalErrorException{
        //I use this test so I can add an user in my repository, cuz I am too lazy to make a function just to add an user to my repository :3
        testCostumerRegistrationOK();

        //estoy pudiendo tener autos con initial date en 2030 con toll passes de 2024. . .
        Optional<List<Double>> paymentList = paymentController.paymentInquiry(LocalDate.of(2020, 1, 1), LocalDate.of(2026, 5, 22));
        System.out.println(paymentList.toString());
        System.out.println(repository.getAllUsers().get(0).toString());
    }

    //TODO realizar los test con error de los paymentInquiry 👍
}
