package com.traffic.client.Interface.local.impl;

import com.traffic.client.Interface.local.ClientController;
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
import com.traffic.events.*;
import com.traffic.exceptions.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@ApplicationScoped
public class ClientControllerImpl implements ClientController {

    @Inject
    private AccountService accountService; //operaciones de la cuenta.

    @Inject
    private UserService userService; //operaciones del usuario.

    @Inject
    private VehicleService vehicleService; //operaciones del vehiculo.

    @Inject
    private Event<CustomEvent> event;

    private void fireNewUserEvent(UserDTO user){
       event.fire(new NewUserEvent("Se ha registrado un nuevo cliente " +
                "en el modulo de gestion. Nombre: " + user.getName()
                ,user));
    }

    @Override
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
                    userDTO.getPassword(), userDTO.getEmail(), userDTO.getId());

        } else if (userDTO instanceof ForeignUserDTO) {
            tollCustomer = new TollCustomer(userDTO.getId(), null, null);
            //armo usuario extranjero.
            user = new ForeignUser(null, tollCustomer, userDTO.getCi(), userDTO.getName(),
                    userDTO.getPassword(), userDTO.getEmail(), userDTO.getId());

        }

        try{
            userService.registerUser(user);
            fireNewUserEvent(userDTO);
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
    }


    private void fireNotEnoughBalanceEvent(User user){
        event.fire(new NotEnoughBalanceEvent("El usuario " + user.getName() + " no tiene saldo suficiente.", user.getId()));
    }

    @Override
    public void throwEvent(TagDTO tagDTO) throws Exception {

        try{
            UUID uuid = UUID.fromString(tagDTO.getUniqueId());

            Tag tag = new Tag(tagDTO.getId(), uuid);

            Optional<User> userOPT = accountService.throwEvent(tag);

            if(userOPT.isPresent()){
                User user = userOPT.get();
                fireNotEnoughBalanceEvent(user);
            }

        }catch (Exception e){
            System.err.println(e.getMessage());
            throw e;
        }

    }


    private void fireVehicleAddedEvent(Long id, VehicleDTO vehicleDTO){
        event.fire(new VehicleAddedEvent("El usuario con id: " + id +
                "Agrego el vehiculo con id unico: " + vehicleDTO.getTagDTO().getUniqueId(),
        id, vehicleDTO));
    }

    @Override
    public void linkVehicle(Long id, VehicleDTO vehicleDTO) throws IllegalArgumentException {

        if(vehicleDTO != null){ //si no es vacio.

            Vehicle vehicle = null;

            //armo objeto vehiculo.
            if(vehicleDTO instanceof NationalVehicleDTO){

                UUID uuid = UUID.fromString(vehicleDTO.getTagDTO().getUniqueId());

                Tag tag = new Tag(vehicleDTO.getTagDTO().getId(), uuid);

                LicensePlate plate = new LicensePlate(((NationalVehicleDTO) vehicleDTO).getLicensePlateDTO().getLicensePlateNumber());

                vehicle = new NationalVehicle(vehicleDTO.getId(), tag,
                        null, plate);
            }else if (vehicleDTO instanceof ForeignVehicleDTO){

                UUID uuid = UUID.fromString(vehicleDTO.getTagDTO().getUniqueId());

                Tag tag = new Tag(vehicleDTO.getTagDTO().getId(), uuid);


                vehicle = new ForeignVehicle(vehicleDTO.getId(), tag, null);
            }

            try{
                vehicleService.linkVehicle(id, vehicle);

                fireVehicleAddedEvent(id, vehicleDTO);

            } catch (Exception e){
                System.err.println(e.getMessage());
                throw e;
            }

        }else{
            throw new IllegalArgumentException("El vehiculo esta vacio.");
        }


    }

    private void fireVehicleRemovedEvent(Long userId, Long vehicleId){
        event.fire(new VehicleRemovedEvent("El usuario con id: " + userId +
                " eliminó el vehiculo con id: " + vehicleId, userId, vehicleId));
    }

    @Override
    public void unLinkVehicle(Long id, Long vehicleId) throws IllegalArgumentException, InvalidVehicleException {
        if (vehicleId != null){
            try{
                vehicleService.unLinkVehicle(id, vehicleId);
                fireVehicleRemovedEvent(id, vehicleId);

            }catch(Exception e){
                System.err.println(e.getMessage());
                throw e;
            }
        }else{
            throw new InvalidVehicleException("El vehiculo no existe.");
        }

    }


    @Override
    public Optional<List<VehicleDTO>> showLinkedVehicles(Long id) throws IllegalArgumentException {

        Optional<List<Vehicle>> vehicleOptionalList =  vehicleService.getLinkedVehicles(id);

        List<Vehicle> vehicleList;
        List<VehicleDTO> vehicleDTOList = new ArrayList<>();
        VehicleDTO vehicleObjectDTO = null;


        List<TollPassDTO> tollPassDTOList = new ArrayList<>();
        List<TollPass> tollPassList;
        TollPassDTO tollPassObjectDTO;

        if(vehicleOptionalList.isPresent()){

            vehicleList = vehicleOptionalList.get();

            for (Vehicle vehicle : vehicleList){

                tollPassList = vehicle.getTollPass();

                if(tollPassList != null){
                    for (TollPass tollPass : tollPassList){

                    tollPassObjectDTO = new TollPassDTO(tollPass.getId(),tollPass.getPassDate().toString(),
                            tollPass.getCost(), tollPass.getPaymentType());

                        tollPassDTOList.add(tollPassObjectDTO);
                    }
                }

                if(vehicle instanceof NationalVehicle){

                    TagDTO tag = new TagDTO(vehicle.getTag().getId(), vehicle.getTag().getUniqueId().toString());

                    LicensePlateDTO plate = new LicensePlateDTO(((NationalVehicle) vehicle).getPlate().getId() ,((NationalVehicle) vehicle).getPlate().getLicensePlateNumber());

                    vehicleObjectDTO = new NationalVehicleDTO(vehicle.getId(), tollPassDTOList, tag, plate);
                } else if (vehicle instanceof ForeignVehicle) {

                    TagDTO tag = new TagDTO(vehicle.getTag().getId(), vehicle.getTag().getUniqueId().toString());

                    vehicleObjectDTO = new ForeignVehicleDTO(vehicle.getId(), tollPassDTOList, tag);
                }

                vehicleDTOList.add(vehicleObjectDTO);
            }

            return Optional.of(vehicleDTOList);

        }

        return Optional.empty();
    }

    @Override
    public void loadBalance(Long id, Double balance) throws Exception {

        try{
            accountService.loadBalance(id, balance);
        }catch (Exception e){
            System.err.println(e.getMessage());
            throw e;
        }

    }

    @Override
    public Optional<Double> showBalance(Long id) throws IllegalArgumentException {

        try{
            return accountService.showBalance(id);

        }catch(Exception e){

            System.err.println(e.getMessage());
            throw e;
        }

    }

    @Override
    public void linkCreditCard(Long id, CreditCardDTO creditCard) throws IllegalArgumentException {

        if(creditCard == null){
            throw new IllegalArgumentException("La tarjeta ingresada no existe");
        }

        LocalDate expireDate = LocalDate.parse(creditCard.getExpireDate(), DateTimeFormatter.ISO_DATE);

        CreditCard card = new CreditCard(creditCard.getId(), creditCard.getCardNumber(),
                creditCard.getName(), expireDate);

        accountService.linkCreditCard(id, card);
    }

    @Override
    public Optional<List<TollPassDTO>> showPastPassages(Long id, LocalDate from, LocalDate to) throws IllegalArgumentException, IllegalRangeException{

        long difference = ChronoUnit.DAYS.between(from, to);

        if(difference < 0){
            throw new IllegalRangeException("El rango de fechas es invalido.");
        }

        try{
            Optional<List<TollPass>> tollPassList = vehicleService.getTollPass(id, from, to);

            return  tollPassListToTollPassDTOList(tollPassList);
        }catch (NoSuchElementException e){
            System.out.println("No se pudieron encontrar las pasadas. " + e.getMessage());
            throw e;
        }

    }

    @Override
    public Optional<List<TollPassDTO>> showPastPassagesVehicle(Long tagId, LocalDate from, LocalDate to) throws IllegalArgumentException, IllegalRangeException{

        long difference = ChronoUnit.DAYS.between(from, to);

        if(difference < 0){
            throw new IllegalRangeException("El rango de fechas es invalido.");
        }

        if(tagId == null){
            throw new IllegalArgumentException("El tag esta vacío o es invalido");
        }

        try{
            Optional<List<TollPass>> tollPassList = vehicleService.getTollPassByVehicle(tagId, from , to);

            return tollPassListToTollPassDTOList(tollPassList);
        }catch (NoSuchElementException e){
            System.out.println("No se pudieron encontrar las pasadas del vehiculo. " + e.getMessage());
            throw e;
        }

    }

    @Override //tic
    public Optional<List<AccountDTO>> getAccountByTag(TagDTO tagDTO) throws IllegalArgumentException {

        if(tagDTO == null){
            throw new IllegalArgumentException("Tipo de tag invalido");
        }

        UUID uuid = UUID.fromString(tagDTO.getUniqueId());

        Tag tag = new Tag(tagDTO.getId(), uuid);

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

                    CreditCardDTO card = null;

                    if(((POSTPay) account).getCreditCard() != null){
                        //en este caso armo la tarjeta para hacerla DTO.
                        String name = ((POSTPay) account).getCreditCard().getName();
                        Long cardId = ((POSTPay) account).getCreditCard().getId();
                        String number = ((POSTPay) account).getCreditCard().getCardNumber();
                        String expireDate = ((POSTPay) account).getCreditCard().getExpireDate().toString();

                        card = new CreditCardDTO(cardId, number,name, expireDate);
                    }

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
    public void prePay(Double balance, TagDTO tagDTO) throws Exception {

        if(tagDTO == null){
            throw new IllegalArgumentException("No se encontró el tag");
        }

        UUID uuid = UUID.fromString(tagDTO.getUniqueId());
        Tag tag = new Tag(tagDTO.getId(), uuid);

        try{

            accountService.prePay(tag, balance);

        }catch (Exception e){
            System.err.println(e.getMessage());
            throw e;
        }

    }


    @Override
    public void postPay(Double balance, TagDTO tagDTO) throws Exception {

        UUID uuid = UUID.fromString(tagDTO.getUniqueId());
        Tag tag = new Tag(tagDTO.getId(), uuid);

        try{
            accountService.postPay(tag, balance);

        }catch(ExternalApiException e){
            throw e;

        }catch(Exception e){
            System.err.println(e.getMessage());
            throw e;
        }

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

                tollPassDTO = new TollPassDTO(tollPass.getId(),tollPass.getPassDate().toString(),
                        tollPass.getCost(), tollPass.getPaymentType());

                tollPassDTOList.add(tollPassDTO);
            }

            return Optional.of(tollPassDTOList);
        }

        return Optional.empty();
    }

}
