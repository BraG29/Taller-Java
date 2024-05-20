package com.traffic.client.Interface.impl;

import com.traffic.client.Interface.ClientController;
import com.traffic.client.application.AccountService;
import com.traffic.client.application.UserService;
import com.traffic.client.application.VehicleService;
import com.traffic.client.application.impl.AccountServiceImpl;
import com.traffic.client.application.impl.UserServiceImpl;
import com.traffic.client.application.impl.VehicleServiceImpl;
import com.traffic.client.domain.Account.CreditCard;
import com.traffic.client.domain.User.ForeignUser;
import com.traffic.client.domain.User.User;
import com.traffic.client.domain.Vehicle.*;
import com.traffic.client.domain.repository.ClientModuleRepository;
import com.traffic.client.domain.repository.ClientModuleRepositoryImpl;
import com.traffic.communication.Interface.CommunicationController;
import com.traffic.communication.Interface.NotificationRepository;
import com.traffic.communication.Interface.impl.CommunicationControllerImpl;
import com.traffic.communication.Interface.impl.NotificationRepositoryImpl;
import com.traffic.dtos.account.AccountDTO;
import com.traffic.dtos.account.CreditCardDTO;
import com.traffic.dtos.account.PostPayDTO;
import com.traffic.dtos.account.PrePayDTO;
import com.traffic.dtos.user.ForeignUserDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.dtos.vehicle.*;
import com.traffic.events.CustomEvent;
import com.traffic.exceptions.IllegalRangeException;
import com.traffic.exceptions.InvalidVehicleException;
import com.traffic.exceptions.NoCustomerException;
import com.traffic.monitoring.Interface.MonitoringController;
import com.traffic.monitoring.Interface.impl.MonitoringControllerImpl;
import com.traffic.payment.Interface.PaymentController;
import com.traffic.payment.Interface.impl.PaymentControllerImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.weld.junit.MockBean;
import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(WeldJunit5Extension.class)
@EnableWeld
@AddBeanClasses({
        ClientControllerImpl.class,
        UserServiceImpl.class,
        AccountServiceImpl.class,
        VehicleServiceImpl.class,
        ClientModuleRepositoryImpl.class
})
class ClientControllerImplTest {

    @WeldSetup
    public static WeldInitiator weld = WeldInitiator
            .of(ClientControllerImpl.class,
                    UserServiceImpl.class,
                    AccountServiceImpl.class,
                    VehicleServiceImpl.class,
                    ClientModuleRepositoryImpl.class,
                    CommunicationControllerImpl.class,
                    NotificationRepositoryImpl.class,
                    PaymentControllerImpl.class,
                    MonitoringControllerImpl.class,
                    CustomEvent.class);

    @Inject
    ClientControllerImpl controller;

    @Inject
    ClientModuleRepositoryImpl repo;

/*    @BeforeAll
    public static void setup(){

       weld = WeldInitiator.from(ClientControllerImpl.class)
                .addBeans(MockBean.builder()
                        .types(ClientModuleRepository.class)
                        .scope(ApplicationScoped.class)
                        .creating(new ClientModuleRepositoryImpl())
                        .build())
                .addBeans(MockBean.builder()
                        .types(AccountService.class)
                        .scope(ApplicationScoped.class)
                        .creating(new AccountServiceImpl())
                        .build())
                .addBeans(MockBean.builder()
                        .types(VehicleService.class)
                        .scope(ApplicationScoped.class)
                        .creating(new VehicleServiceImpl())
                        .build())
                .addBeans(MockBean.builder()
                        .types(UserService.class)
                        .scope(ApplicationScoped.class)
                        .creating(new UserServiceImpl())
                        .build())
               .addBeans(MockBean.builder()
                       .types(CommunicationController.class)
                       .scope(ApplicationScoped.class)
                       .creating(new UserServiceImpl())
                       .build())
               .addBeans(MockBean.builder()
                       .types(NotificationRepository.class)
                       .scope(ApplicationScoped.class)
                       .creating(new UserServiceImpl())
                       .build())
               .addBeans(MockBean.builder()
                       .types(PaymentController.class)
                       .scope(ApplicationScoped.class)
                       .creating(new UserServiceImpl())
                       .build())
               .addBeans(MockBean.builder()
                       .types(MonitoringController.class)
                       .scope(ApplicationScoped.class)
                       .creating(new UserServiceImpl())
                       .build())
               .addBeans(MockBean.builder()
                       .types(CustomEvent.class)
                       .scope(ApplicationScoped.class)
                       .creating(new UserServiceImpl())
                       .build())
                .build();
    }*/


    @Test //tic
    void listUsers(ClientController controller ){

        System.out.println("Lista  de usuarios del sistema.");
        for(User usr: controller.listUsers().get()){
            System.out.println(usr.getId() + usr.getName() + usr.getCi() + usr.getEmail());
        }

    }

    @Test //tic
    void addTollCostumer(ClientController controller) {

        System.out.println("Lista de usuarios antes de agregar:");

        for(User usr: controller.listUsers().get()){
            System.out.println("Id: " + usr.getId() +" Nombre: " + usr.getName() +
                    " Ci: " + usr.getCi()+ " Email: " + usr.getEmail());
        }

        UserDTO testUser = new ForeignUserDTO(32L, "carlitos@mail.com", "1234",
                "Carlitos", "5.321.512-7", null, null, null);

        System.out.println("Agregando usuario...");
        controller.addTollCostumer(testUser);

        System.out.println("Lista de usuarios despues de agregar:");
        for(User usr: controller.listUsers().get()){
            System.out.println("Id: " + usr.getId() +" Nombre: " + usr.getName() +
                    " Ci: " + usr.getCi()+ " Email: " + usr.getEmail());
        }

    }

    @Test //tic
    void linkVehicle(ClientController controller) throws NoCustomerException {
        VehicleDTO linkVehicleTest = new ForeignVehicleDTO(2L,null ,
                new TagDTO(341561L));

        //usr 51 no tiene vehiculos.
        Long userId = 51L;

        System.out.println("Vehiculos del usuario antes de Vincular:");
        if(controller.showLinkedVehicles(userId).isPresent()){

            for(VehicleDTO vehicleDTO: controller.showLinkedVehicles(userId).get()){
                System.out.println("Tag: "+ vehicleDTO.getTagDTO().getUniqueId());
            }

        }else{
            System.out.println("El usuario no tiene vehiculos");
        }

        System.out.println("Agregando vehiculo con TAG 341561");
        controller.linkVehicle(userId, linkVehicleTest);

        System.out.println("Vehiculos del usuario");
        for(VehicleDTO vehicleDTO: controller.showLinkedVehicles(userId).get()){
            System.out.println("Tag: "+ vehicleDTO.getTagDTO().getUniqueId());
        }

    }

    @Test //tic
    void unLinkVehicle(ClientController controller) throws InvalidVehicleException, NoCustomerException {

        TagDTO tagNational = new TagDTO(203123L);

        LicensePlateDTO plate = new LicensePlateDTO(1L, "MAG 2013");

        VehicleDTO unLinkVehicleTest = new NationalVehicleDTO(2L, null, tagNational, plate);

        Long userId = 1L;

        System.out.println("Vehiculos del usuario antes de desvincular:");
        for(VehicleDTO vehicleDTO: controller.showLinkedVehicles(userId).get()){
            System.out.println("Id: "+ vehicleDTO.getTagDTO().getUniqueId());
        }

        System.out.println("Eliminando vehiculo con id tag" + tagNational.getUniqueId());
        controller.unLinkVehicle(userId, unLinkVehicleTest);

        System.out.println("Vehiculos del usuario despues de desvincular:");
        for(VehicleDTO vehicleDTO: controller.showLinkedVehicles(userId).get()){
            System.out.println("Tag: "+ vehicleDTO.getTagDTO().getUniqueId());
        }

    }


    @Test //tic
    void loadBalanceANDShowBalance(ClientController controller) throws NoCustomerException {

        controller.loadBalance(51L, 2000D);
        System.out.println("Cargo balance al usuario con id 51");

        System.out.println("Balance con funcion showBalance:  " + controller.showBalance(51L).get());

        System.out.println("Buscando usuario en la lista de usuarios");
        for(User usr: controller.listUsers().get()){
            if(usr.getId().equals(51L)){
                System.out.println("Id: "+ usr.getId() +" Nombre: " + usr.getName() + " CI: " + usr.getCi() +
                       " Email: " + usr.getEmail() + " Balance: " + usr.getTollCustomer().getPrePay().getBalance());
            }
        }

    }

    @Test //tic
    void linkCreditCard(ClientController controller) throws NoCustomerException {

        //id 51 no tiene cuentas.
        CreditCardDTO card = new CreditCardDTO( 51L, "5423-1256-7437-1278"," Pepe", LocalDate.now());

        System.out.println("Agregando tarjeta: " + card.getCardNumber()
        + " al usuario con id: 51");

        controller.linkCreditCard(51L, card);

        System.out.println("Buscando usuario en la lista de usuarios");
        for(User usr: controller.listUsers().get()){
            if(usr.getId().equals(51L)){
                System.out.println("Id: "+ usr.getId() +" Nombre: " + usr.getName() + " CI: " + usr.getCi() +
                        " Email: " + usr.getEmail() + " Tarjeta:  " + usr.getTollCustomer().getPostPay().getCreditCard().getCardNumber());
            }
        }

        //si quiero q falle
        //controller.linkCreditCard(51L, null);

    }

    @Test //tic
    void showPastPassages(ClientController controller) throws IllegalRangeException, NoCustomerException {

        Optional<List<TollPassDTO>> tollPassDTOListOPT = controller.showPastPassages(2L, LocalDate.now(), LocalDate.now());
        if(tollPassDTOListOPT.isPresent()){

            List<TollPassDTO> tollPassDTOList = tollPassDTOListOPT.get();
            Integer i = 0;
            for (TollPassDTO pass : tollPassDTOList){
            System.out.println("Pasada Nº: " + i + " Tipo de pago: " + pass.getPaymentType()
                    + " Costo: " + pass.getCost() + " Fecha: " + pass.getDate());
            i++;

            }
        }else {
            System.out.println("Nada q mostrar.");
        }
    }

    @Test //tic
    void showPastPassagesVehicle(ClientController controller) throws IllegalRangeException, NoCustomerException {

        //203123L sin pasadas
        //253897L con pasadas
        TagDTO tag = new TagDTO(203123L);

        Optional<List<TollPassDTO>> tollPassDTOListOPT = controller.showPastPassagesVehicle(tag, LocalDate.now(), LocalDate.now());

        if(tollPassDTOListOPT.isPresent()){
            List<TollPassDTO> tollPassDTOList = tollPassDTOListOPT.get();
            Integer i = 0;
            for (TollPassDTO pass : tollPassDTOList){
                System.out.println("Pasada Nº: " + i + " Tipo de pago: " + pass.getPaymentType()
                        + " Costo: " + pass.getCost() + " Fecha: " + pass.getDate());
                i++;
            }
        }else {
            System.out.println("El vehiculo no tiene pasadas.");
        }

    }

    @Test //tic
    void getAccountByTag(ClientController controller) {
        //user 1
        //203123L
        //253897L
        //user2
        //123512L

        TagDTO tag = new TagDTO(123512L);

        Optional<List<AccountDTO>> accountOPT = controller.getAccountByTag(tag);

        if(accountOPT.isPresent()){

            for(AccountDTO account : accountOPT.get()){
                String accountType = null;

                if (account instanceof PrePayDTO) {
                    PrePayDTO prePayAcc = (PrePayDTO) account;
                    accountType = "PrePaga";

                    System.out.println("Fecha creacion: " + prePayAcc.getCreationDate() +
                            ", Nro cuenta: " + prePayAcc.getAccountNumber() + ", Tipo de cuenta: " + accountType +
                            ", Saldo: " + prePayAcc.getBalance());

                } else if (account instanceof PostPayDTO) {

                    accountType = "PostPaga";
                    PostPayDTO postPayAcc = (PostPayDTO) account;

                    System.out.println("Fecha creacion: " + postPayAcc.getCreationDate() +
                            ", Nro cuenta: " + postPayAcc.getAccountNumber() + ", Tipo de cuenta: " + accountType +
                            ", Nro Tarjeta: " + postPayAcc.getCreditCardDTO().getCardNumber());
                }

            }
        }

    }

    @Test
    void prePay() {
    }

    @Test
    void postPay() {
    }

}