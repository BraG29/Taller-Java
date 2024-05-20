package com.traffic.client.Interface.impl;

import com.traffic.client.Interface.ClientController;
import com.traffic.client.application.AccountService;
import com.traffic.client.application.UserService;
import com.traffic.client.application.VehicleService;
import com.traffic.client.domain.Account.Account;
import com.traffic.client.domain.Account.CreditCard;
import com.traffic.client.domain.Account.POSTPay;
import com.traffic.client.domain.Account.PREPay;
import com.traffic.client.domain.User.ForeignUser;
import com.traffic.client.domain.User.NationalUser;
import com.traffic.client.domain.User.TollCustomer;
import com.traffic.client.domain.User.User;
import com.traffic.client.domain.Vehicle.*;
import com.traffic.dtos.account.AccountDTO;
import com.traffic.dtos.account.CreditCardDTO;
import com.traffic.dtos.account.PostPayDTO;
import com.traffic.dtos.account.PrePayDTO;
import com.traffic.dtos.user.ForeignUserDTO;
import com.traffic.dtos.user.NationalUserDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.dtos.vehicle.*;
import com.traffic.exceptions.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ApplicationScoped
public class ClientControllerImpl implements ClientController {


    @Inject
    private AccountService accountService; //operaciones de la cuenta.

    @Inject
    private UserService userService; //operaciones del usuario.

    @Inject
    private VehicleService vehicleService;

    @Override //tic
    public void addTollCostumer(UserDTO userDTO) throws IllegalArgumentException {

        if(userDTO == null){
            throw new IllegalArgumentException("El usuario recibido está vacío");
        }

        User user = null;

        TollCustomer tollCustomer;

        //se arma el usuario, con cliente sin cuentas, y sin vinculos.
        if(userDTO instanceof NationalUserDTO){

            //armo usuario nacional
            tollCustomer = new TollCustomer(userDTO.getId(), null, null);
            user = new NationalUser(null,  tollCustomer, userDTO.getCi(), userDTO.getName(),
                    userDTO.getPassword(), userDTO.getEmail(), userDTO.getId(), null);

        } else if (userDTO instanceof ForeignUserDTO) {
            tollCustomer = new TollCustomer(userDTO.getId(), null, null);
            //armo usuario extranjero.
            user = new ForeignUser(null, tollCustomer, userDTO.getCi(), userDTO.getName(),
                    userDTO.getPassword(), userDTO.getEmail(), userDTO.getId());

        }

        userService.registerUser(user);
        //TODO TIRAR evento de registro usr
    }

    @Override //tic
    public void linkVehicle(Long id, VehicleDTO vehicleDTO) throws IllegalArgumentException, NoCustomerException {

        if(vehicleDTO != null){ //si no es vacio.

            List<TollPass> listTollPass = null; //armo una lista de pasadas.

            List<TollPassDTO> listTollPassDTO = vehicleDTO.getTollPassDTO(); //obtengo lista de pasadas

            TollPass tollPassObject = null;

            Vehicle vehicle = null;
            if(listTollPassDTO != null){
                for (TollPassDTO tollPassDTO : listTollPassDTO){

                    tollPassObject = new TollPass(
                            tollPassDTO.getDate(), tollPassDTO.getCost(),
                            tollPassDTO.getPaymentType());
                    listTollPass.add(tollPassObject);//armo la lista de pasadas con el DTO de pasadas.


                }
            }


            //armo objeto vehiculo.
            if(vehicleDTO instanceof NationalVehicleDTO){

                Tag tag = new Tag(vehicleDTO.getTagDTO().getUniqueId());
                LicensePlate plate = new LicensePlate(((NationalVehicleDTO) vehicleDTO).getLicensePlateDTO().getLicensePlateNumber());

                vehicle = new NationalVehicle(vehicleDTO.getId(), tag,
                        listTollPass, plate);
            }else if (vehicleDTO instanceof ForeignVehicleDTO){
                Tag tag = new Tag(vehicleDTO.getTagDTO().getUniqueId());

                vehicle = new ForeignVehicle(vehicleDTO.getId(), tag, listTollPass);
            }

            vehicleService.linkVehicle(id, vehicle);
        }

    }

    @Override //tic
    public void unLinkVehicle(Long id, VehicleDTO vehicleDTO) throws IllegalArgumentException, InvalidVehicleException, NoCustomerException {
        if (vehicleDTO != null){

            List<TollPass> listTollPass = new ArrayList<>();

            List<TollPassDTO> listTollPassDTO = vehicleDTO.getTollPassDTO();

            TollPass tollPassObject = null;

            Vehicle vehicle = null;


            if(listTollPassDTO != null){

            for (TollPassDTO tollPassDTO : listTollPassDTO){

                tollPassObject = new TollPass(tollPassDTO.getDate(), tollPassDTO.getCost(),
                        tollPassDTO.getPaymentType());
                listTollPass.add(tollPassObject);

                }
            }

            if(vehicleDTO instanceof NationalVehicleDTO){

                Tag tag = new Tag(vehicleDTO.getTagDTO().getUniqueId());
                LicensePlate plate = new LicensePlate(((NationalVehicleDTO) vehicleDTO).getLicensePlateDTO().getLicensePlateNumber());

                vehicle = new NationalVehicle(vehicleDTO.getId(), tag,
                        listTollPass, plate);

            }else if (vehicleDTO instanceof ForeignVehicleDTO){
                Tag tag = new Tag(vehicleDTO.getTagDTO().getUniqueId());

                vehicle = new ForeignVehicle(vehicleDTO.getId(), tag, listTollPass);
            }

            vehicleService.unLinkVehicle(id, vehicle);

        }

    }


    @Override //tic
    public Optional<List<VehicleDTO>> showLinkedVehicles(Long id) throws IllegalArgumentException, NoCustomerException {

        Optional<List<Vehicle>> vehicleOptionalList =  vehicleService.getLinkedVehicles(id);

        List<Vehicle> vehicleList;
        List<VehicleDTO> vehicleDTOList = new ArrayList<>();
        VehicleDTO vehicleObjectDTO = null;


        List<TollPassDTO> tollPassDTOList = new ArrayList<>();
        List<TollPass> tollPassList = null;
        TollPassDTO tollPassObjectDTO;

        if(vehicleOptionalList.isPresent()){

            vehicleList = vehicleOptionalList.get();

            for (Vehicle vehicle : vehicleList){

                tollPassList = vehicle.getTollPass();

                if(tollPassList != null){
                    for (TollPass tollPass : tollPassList){

                    tollPassObjectDTO = new TollPassDTO(tollPass.getPassDate(),
                            tollPass.getCost(), tollPass.getPaymentType());

                        tollPassDTOList.add(tollPassObjectDTO);
                    }
                }

                if(vehicle instanceof NationalVehicle){

                    TagDTO tag = new TagDTO(vehicle.getTag().getTagId());

                    LicensePlateDTO plate = new LicensePlateDTO(((NationalVehicle) vehicle).getPlate().getId() ,((NationalVehicle) vehicle).getPlate().getLicensePlateNumber());

                    vehicleObjectDTO = new NationalVehicleDTO(vehicle.getId(), tollPassDTOList, tag, plate);
                } else if (vehicle instanceof ForeignVehicle) {

                    TagDTO tag = new TagDTO(vehicle.getTag().getTagId());

                    vehicleObjectDTO = new ForeignVehicleDTO(vehicle.getId(), tollPassDTOList, tag);
                }

                vehicleDTOList.add(vehicleObjectDTO);
            }

            return Optional.of(vehicleDTOList);

        }

        return Optional.empty();
    }

    @Override //tic
    public void loadBalance(Long id, Double balance) throws IllegalArgumentException, NoCustomerException {

        accountService.loadBalance(id, balance);


    }

    @Override //tic
    public Optional<Double> showBalance(Long id) throws IllegalArgumentException, NoCustomerException {

        return accountService.showBalance(id);
    }

    @Override //tic
    public void linkCreditCard(Long id, CreditCardDTO creditCard) throws IllegalArgumentException, NoCustomerException {

        if(creditCard == null){
            throw new IllegalArgumentException("La tarjeta ingresada no existe");
        }

        CreditCard card = new CreditCard(creditCard.getId(), creditCard.getCardNumber(),
                creditCard.getName(), creditCard.getExpireDate());


        accountService.linkCreditCard(id, card);


    }

    @Override //tic
    public Optional<List<TollPassDTO>> showPastPassages(Long id, LocalDate from, LocalDate to) throws IllegalArgumentException, IllegalRangeException, NoCustomerException {

        long difference = ChronoUnit.DAYS.between(from, to);

        if(difference < 0){
            throw new IllegalRangeException("El rango de fechas es invalido.");
        }

        try{
            Optional<List<TollPass>> tollPassList = vehicleService.getTollPass(id, from, to);

            return  tollPassListToTollPassDTOList(tollPassList);
        }catch (NoSuchElementException e){
            System.out.println("No se pudieron encontrar las pasadas. " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override //tic
    public Optional<List<TollPassDTO>> showPastPassagesVehicle(TagDTO tag, LocalDate from, LocalDate to) throws IllegalArgumentException, IllegalRangeException, NoCustomerException {

        long difference = ChronoUnit.DAYS.between(from, to);

        if(difference < 0){
            throw new IllegalRangeException("El rango de fechas es invalido.");
        }

        if(tag == null){
            throw new IllegalArgumentException("El tag esta vacío o es invalido");
        }

        Tag tagObject = new Tag(tag.getUniqueId());

        try{
            Optional<List<TollPass>> tollPassList = vehicleService.getTollPassByVehicle(tagObject, from , to);

            return tollPassListToTollPassDTOList(tollPassList);
        }catch (NoSuchElementException e){
            System.out.println("No se pudieron encontrar las pasadas del vehiculo. " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override //tic
    public Optional<List<AccountDTO>> getAccountByTag(TagDTO tagDTO) throws IllegalArgumentException {

        if(tagDTO == null){
            throw new IllegalArgumentException("Tipo de tag invalido");
        }

        Tag tag = new Tag(tagDTO.getUniqueId());

        Optional<List<Account>> accounts = accountService.getAccountByTag(tag);

        List<AccountDTO> accountsDTO = new ArrayList<>();

        if(accounts.isPresent()){//si hay valores dentro del optional.

            List<Account> accountsList = accounts.get(); //transformo el optional a una lista comun para obtener los elementos.

            for (Account account : accountsList) {

                AccountDTO accDTO = null;

                if(account instanceof PREPay){

                    accDTO = new PrePayDTO(account.getId(), account.getAccountNumber(),
                            account.getCreationDate(), ((PREPay) account).getBalance()); //convierto de Account a PrePay

                } else if (account instanceof POSTPay) {

                    //en este caso armo la tarjeta para hacerla DTO.
                    String name = ((POSTPay) account).getCreditCard().getName();
                    Long cardId = ((POSTPay) account).getCreditCard().getId();
                    String number = ((POSTPay) account).getCreditCard().getCardNumber();
                    LocalDate expireDate = ((POSTPay) account).getCreditCard().getExpireDate();

                    CreditCardDTO card = new CreditCardDTO(cardId, number,name, expireDate);

                    accDTO = new PostPayDTO(account.getId(), account.getAccountNumber(),
                            account.getCreationDate(), card); //convierto de Account a PostPay
                }

                accountsDTO.add(accDTO); //añado a la lista
            }

            return Optional.of(accountsDTO);
        }

        return Optional.empty();
    }

    @Override
    public Boolean prePay(Double balance, TagDTO tagDTO) throws NoAccountException, IllegalArgumentException, NoCustomerException {

        if(tagDTO == null){
            throw new NoCustomerException();
        }

        Tag tag = new Tag(tagDTO.getUniqueId());

        try{
            return accountService.prePay(tag, balance);

        }catch (NoAccountException | IllegalArgumentException | NoCustomerException e){
            System.out.println("Ocurrio un error: " + e.getMessage());
        }

        return false;
    }

    @Override
    public Boolean postPay(Double balance, TagDTO tagDTO) throws IllegalArgumentException, NoCustomerException {

        //TODO revisar.
        Tag tag = new Tag(tagDTO.getUniqueId());

        try{
            return accountService.postPay(tag, balance);

        }catch(IllegalArgumentException | NoCustomerException | NoAccountException | ExternalApiException |
               InvalidVehicleException e){
            System.out.println("Ocurrio un error: " + e.getMessage());
        }

        return null;
    }

    @Override //tic
    public Optional<List<User>> listUsers() {
        return userService.showUsers();
    }

    //funcion auxiliar de pasaje lista TollPass a lista TollPassDTO.
    private Optional<List<TollPassDTO>> tollPassListToTollPassDTOList(Optional<List<TollPass>> tollPassList){

        List<TollPass> listTollPass;

        List<TollPassDTO> tollPassDTOList = new ArrayList<>();
        TollPassDTO tollPassDTO;

        if(tollPassList.isPresent()){

            listTollPass = tollPassList.get();

            for(TollPass tollPass : listTollPass){

                tollPassDTO = new TollPassDTO(tollPass.getPassDate(),
                        tollPass.getCost(), tollPass.getPaymentType());

                tollPassDTOList.add(tollPassDTO);
            }

            return Optional.of(tollPassDTOList);
        }

        return Optional.empty();
    }

}
