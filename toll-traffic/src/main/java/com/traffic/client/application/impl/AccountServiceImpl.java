package com.traffic.client.application.impl;

import com.traffic.client.application.AccountService;
import com.traffic.client.domain.Account.Account;
import com.traffic.client.domain.Account.CreditCard;
import com.traffic.client.domain.Account.POSTPay;
import com.traffic.client.domain.Account.PREPay;
import com.traffic.client.domain.User.ForeignUser;
import com.traffic.client.domain.User.NationalUser;
import com.traffic.client.domain.User.User;
import com.traffic.client.domain.Vehicle.*;
import com.traffic.client.domain.repository.ClientModuleRepository;
import com.traffic.communication.Interface.CommunicationController;
import com.traffic.dtos.account.CreditCardDTO;
import com.traffic.dtos.account.PostPayDTO;
import com.traffic.dtos.account.PrePayDTO;
import com.traffic.dtos.user.ForeignUserDTO;
import com.traffic.dtos.user.NationalUserDTO;
import com.traffic.dtos.user.TollCustomerDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.dtos.vehicle.*;
import com.traffic.exceptions.ExternalApiException;
import com.traffic.exceptions.InvalidVehicleException;
import com.traffic.exceptions.NoCustomerException;
import com.traffic.monitoring.Interface.MonitoringController;
import com.traffic.payment.Interface.PaymentController;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

@ApplicationScoped
public class AccountServiceImpl implements AccountService {

    @Inject
    private ClientModuleRepository repo;

    @Inject
    private PaymentController paymentController;

    @Inject
    private MonitoringController monitoringController;
    
    @Inject
    private CommunicationController communicationController;

    @Override
    public void prePay(Tag tag, Double cost) throws NoCustomerException {
        
        User usr = repo.findByTag(tag);
        
        UserDTO userDTO = null;
        
        if(usr != null && usr.getTollCustomer() != null){

            PREPay prePay = null;
            
            if(usr.getTollCustomer().getPrePay() != null){
              
               prePay = usr.getTollCustomer().getPrePay();
             
               if(cost > prePay.getBalance()){
                   monitoringController.notifyNotEnoughBalance();
                   
                   //lanzonotificacion armo UserDTO con cosas basicas.
                   if(usr instanceof NationalUser){
                       userDTO = new NationalUserDTO(usr.getId(), usr.getEmail(), usr.getPassword(),
                               usr.getName(), usr.getCi(), new TollCustomerDTO(), null, null,
                               null);
                   }else if (usr instanceof ForeignUser){
                       userDTO = new ForeignUserDTO(usr.getId(), usr.getEmail(), usr.getPassword(),
                               usr.getName(), usr.getCi(), new TollCustomerDTO(), null, null);
                   }

                   communicationController.notifyNotEnoughBalance(userDTO);

                   //TODO tiro excepcion.
               }else{
                   prePay.pay(cost);
                   //TODO evento noitifcar modulo monitoreo
               }
              
            }//No hay  cuenta prepaga
            
        } // no existe usuario o no es Cliente
            //throw new NoCustomerException("El cliente no existe");
        
    }

    @Override
    public void postPay(Tag tag, Double cost) throws ExternalApiException, InvalidVehicleException, NoCustomerException {

        User usr = repo.findByTag(tag);

        UserDTO userDTO = null;

        if(usr != null && usr.getTollCustomer() != null){

            TollCustomerDTO customerDTO = null;

            if(usr.getTollCustomer().getPostPay() == null){

                //TODO si no tiene cuenta postpay rechazo pago. Y tiro exception

                monitoringController.notifyCreditCardPaymentRejected();
            }

             //Armo cuenta prepago y cliente para enviar notificacion de pago al modulo de medios de pago.
            customerDTO = getTollCustomerDTO(usr);

            List<Link> vehicles = usr.getLinkedCars();
            List<LinkDTO> linkListDTO = new ArrayList<>();
            LinkDTO linkObject;

            List<TollPass> listTollPass;
            List<TollPassDTO> listTollPassDTO = new ArrayList<>();
            TollPassDTO tollPassObject;

            Vehicle vehicle;
            VehicleDTO vehicleDTO = null;
            LicensePlateDTO licencePlate;
            TagDTO tagDTO;

            for (Link link : vehicles) {
                if (link.getVehicle().getTag().getTagId().equals(tag.getTagId())) { //Si el vehiculo esta en la lista
                    vehicle = link.getVehicle();

                    listTollPass = vehicle.getTollPass();

                    for (TollPass tollPass : listTollPass){ //obtengo pasadas del vehiculo, y las paso a una listaDTO.
                        tollPassObject = new TollPassDTO(tollPass.getId(), tollPass.getPassDate(), tollPass.getCost(), tollPass.getPaymentType());
                        listTollPassDTO.add(tollPassObject);
                    }

                    if(vehicle instanceof NationalVehicle){

                        tagDTO = new TagDTO(vehicle.getTag().getTagId());
                        licencePlate = new LicensePlateDTO(((NationalVehicle) vehicle).getPlate().getId() ,((NationalVehicle) vehicle).getPlate().getLicensePlateNumber());
                        vehicleDTO = new NationalVehicleDTO(vehicle.getId(), listTollPassDTO, tagDTO, licencePlate);

                    } else if (vehicle instanceof  ForeignVehicle){
                        tagDTO = new TagDTO(vehicle.getTag().getTagId());
                        vehicleDTO = new ForeignVehicleDTO(vehicle.getId(), listTollPassDTO, tagDTO);
                    }

                    linkObject = new LinkDTO(link.getId(), link.getInitialDate(), link.getActive(), vehicleDTO);
                    linkListDTO.add(linkObject);
                }

            }

            if(usr instanceof NationalUser){
                userDTO = new NationalUserDTO(usr.getId(), usr.getEmail(), usr.getPassword(), usr.getName(),
                        usr.getCi(), customerDTO, linkListDTO, null, null);//Es necesario pasar en el usuario sucive y notificaciones?
            } else if(usr instanceof  ForeignUser){
                userDTO = new ForeignUserDTO(usr.getId(), usr.getEmail(), usr.getPassword(), usr.getName(),
                        usr.getCi(), customerDTO, linkListDTO, null);
            }

            paymentController.notifyPayment(userDTO, vehicleDTO, cost, customerDTO.getPostPayDTO().getCreditCardDTO());
            //TODO evento cobro con tarjeta, modulo comunicacion.
            
        } //TODO exception.  No customer

    }

    @Override
    public Optional<List<Account>> getAccountByTag(Tag tag) {

        User usr = repo.findByTag(tag); //Este usuario lo voy a traer desde un repositorio que  hablara con una BD en el futuro. Injectando uno

        if(usr.getTollCustomer() != null){//si es cliente

            //el usuario siempre tiene prepaga, SI ES CLIENTE.
            List<Account> accounts = new ArrayList<Account>();
            accounts.add(usr.getTollCustomer().getPrePay());

            if(usr.getTollCustomer().getPostPay() != null){ //agrego postpaga, si tiene.

                accounts.add(usr.getTollCustomer().getPostPay());
            }
            return Optional.of(accounts);//devuelvo cuentas

        }//Si el usuario no es cliente devuelvo vacio

        return Optional.empty();

    }

    @Override
    public void loadBalance(Long id, Double balance) {

        User usr = repo.getUserById(id);

        if(usr != null){

            if(usr.getTollCustomer() != null){

                usr.getTollCustomer().getPrePay().loadBalance(balance);
                //TODO actualizar en repo
            }
            //exception
        }
        //Exception

    }

    @Override
    public Optional<Double> showBalance(Long id) {

        User usr = repo.getUserById(id);

        if(usr != null){
            if (usr.getTollCustomer() != null && usr.getTollCustomer().getPrePay() != null){

                return Optional.ofNullable(usr.getTollCustomer().getPrePay().getBalance());

            }//exception
        }//exception

        return Optional.empty();
    }

    @Override
    public void linkCreditCard(Long id, CreditCard creditCard) {

        User usr = repo.getUserById(id);

        POSTPay postPay = new POSTPay();

        LocalDate creationDate = LocalDate.now();

        if (usr.getTollCustomer().getPostPay() == null){ //si no tiene cuenta postPaga le creo una y le agrego la tarjeta.

            Integer accountNumber = postPay.generateRandomAccountNumber();//si postPay se inicializa en null aca tengo nullpointerException.

            postPay = new POSTPay(id,accountNumber, creationDate, creditCard);

            usr.getTollCustomer().setPostPay(postPay);

            //TODO actualizar usuario en repo

        } else{ //si ya tiene cuenta le cambio la tarjeta.

            usr.addCreditCard(creditCard);
            //TODO actualizar usuario en repo
        }

    }


    //funciones auxiliares:

    private static TollCustomerDTO getTollCustomerDTO(User usr) {
        PREPay prePay = null;
        PrePayDTO prePayDTO = null;

        POSTPay postPay = null;
        PostPayDTO postPayDTO = null;

        CreditCard card = null;
        CreditCardDTO cardDTO = null;

        //armo cuenta postpay
        if(usr.getTollCustomer().getPostPay() != null){
            card = usr.getTollCustomer().getPostPay().getCreditCard();
            cardDTO = new CreditCardDTO(card.getId(), card.getCardNumber(), card.getName(), card.getExpireDate());

            postPay = usr.getTollCustomer().getPostPay();
            postPayDTO = new PostPayDTO(postPay.getId(), postPay.getAccountNumber(), postPay.getCreationDate(), cardDTO);

        }

        //armo cuenta prepay
        if(usr.getTollCustomer().getPrePay() != null){
            prePay = usr.getTollCustomer().getPrePay();
            prePayDTO = new PrePayDTO(prePay.getId(), prePay.getAccountNumber(), prePay.getCreationDate(), prePay.getBalance());
        }

        return new TollCustomerDTO(usr.getTollCustomer().getId(), postPayDTO, prePayDTO);
    }
    
}
