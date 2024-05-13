package com.traffic.client.Interface.impl;

import com.traffic.client.Interface.ClientController;
import com.traffic.client.application.AccountService;
import com.traffic.client.application.UserService;
import com.traffic.client.application.VehicleService;
import com.traffic.client.domain.Account.Account;
import com.traffic.client.domain.Account.CreditCard;
import com.traffic.client.domain.Account.POSTPay;
import com.traffic.client.domain.Account.PREPay;
import com.traffic.client.domain.User.*;
import com.traffic.client.domain.Vehicle.*;
import com.traffic.client.domain.tariff.CommonTariff;
import com.traffic.dtos.account.AccountDTO;
import com.traffic.dtos.account.CreditCardDTO;
import com.traffic.dtos.account.PostPayDTO;
import com.traffic.dtos.account.PrePayDTO;
import com.traffic.dtos.tariff.CommonTariffDTO;
import com.traffic.dtos.user.ForeignUserDTO;
import com.traffic.dtos.user.NationalUserDTO;
import com.traffic.dtos.user.SuciveCustomerDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.dtos.vehicle.*;
import com.traffic.exceptions.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ClientControllerImpl implements ClientController {

    @Inject
    private AccountService accountService; //operaciones de la cuenta.

    @Inject
    private UserService userSerivce; //operaciones del usuario.

    @Inject
    private VehicleService vehicleService;

    @Override
    public void addTollCostumer(UserDTO userDTO) throws IllegalArgumentException {

        if(userDTO == null){
            throw new IllegalArgumentException("El usuario recibido no existe");
        }

        Long tollCustomerID = userDTO.getTollCustomer().getId();

        //armo cliente telepeaje
        TollCustomer customer = new TollCustomer(tollCustomerID, null, null);

        //Armado del vehiculo

        //Lista de vinculos
        List<LinkDTO> linkDTO = userDTO.getLinkedVehicles();
        List<Link> linkedVehicles = new ArrayList<>();
        Link linkObject;

        //Lista de pasadas
        List<TollPassDTO> tollPassDTO;
        List<TollPass> tollPassList = new ArrayList<>();
        TollPass pass;

        //objeto vehiculo.
        NationalVehicleDTO nationalVehicleDTO; //vehiculo nacional
        ForeignVehicleDTO foreignVehicleDTO; //vehiculo extranjero
        Vehicle vehicleObject;
        Tag tag;

        if(userDTO instanceof NationalUserDTO){

            //En esta parte hay que armar la lista de vinculos, que tiene vehiculos con una lista de pasadas.
            //Y un objeto tag. Se itera en la lista de DTO, se crea un objeto, y se lo agrega a una lista común.

            SuciveCustomerDTO suciveCustomerDTO = ((NationalUserDTO) userDTO).getSuciveCustomer();
            CommonTariffDTO commonTariffDTO= suciveCustomerDTO.getCommonTariff();


            CommonTariff tariff = new CommonTariff(commonTariffDTO.getAmount(),commonTariffDTO.getId());
            SuciveCustomer suciveCustomer = new SuciveCustomer(suciveCustomerDTO.getId(), tariff);

            for (LinkDTO link : linkDTO) {

                //Armado del objeto vehiculo.
                nationalVehicleDTO = (NationalVehicleDTO) link.getVehicle();

                tollPassDTO = nationalVehicleDTO.getTollPassDTO();
                for (TollPassDTO tollPass : tollPassDTO){ //obtengo pasas del vehiculo en DTO
                    pass = new TollPass(tollPass.getId(), tollPass.getDate(), tollPass.getCost(), tollPass.getPaymentType()); //arreglar esto talvez puedo usar PaymentTypeData en lugar de crear otro enum.
                    tollPassList.add(pass);//guardo pasadas en DTO en lista comun.
                }

                tag = new Tag(nationalVehicleDTO.getTagDTO().getUniqueId()); //paso de TagDTO a tag

                LicensePlate plate = new LicensePlate(
                        nationalVehicleDTO.getLicensePlateDTO().getLicensePlateNumber()); //paso de matriculaDTO a matricula.

                //armo objeto auto, con la lista de pasadas.
                vehicleObject = new NationalVehicle(nationalVehicleDTO.getId(), tag, tollPassList, plate);

                linkObject = new Link(link.getId(), link.getActive(), vehicleObject, link.getInitialDate());

                linkedVehicles.add(linkObject);

            }

            //armo usuario nacional
            NationalUser user = new NationalUser(linkedVehicles, customer, userDTO.getCi(), userDTO.getName(),
                    userDTO.getPassword(), userDTO.getEmail(), userDTO.getId(), suciveCustomer);

            userSerivce.registerUser(user);
            //TODO Tirar evento y try and catch

        } else if (userDTO instanceof ForeignUserDTO) {

            for (LinkDTO link : linkDTO) {

                //Armado del objeto vehiculo.
                foreignVehicleDTO = (ForeignVehicleDTO) link.getVehicle();

                tollPassDTO = foreignVehicleDTO.getTollPassDTO();

                for (TollPassDTO tollPass : tollPassDTO){ //obtengo pasas del vehiculo en DTO
                    pass = new TollPass(tollPass.getId(), tollPass.getDate(), tollPass.getCost(), tollPass.getPaymentType()); //arreglar esto talvez puedo usar PaymentTypeData en lugar de crear otro enum.
                    tollPassList.add(pass);//guardo pasadas en DTO en lista comun.
                }

                tag = new Tag(foreignVehicleDTO .getTagDTO().getUniqueId()); //paso de TagDTO a tag

                //armo objeto auto, con la lista de pasadas.
                vehicleObject = new ForeignVehicle(foreignVehicleDTO.getId(), tag, tollPassList);

                linkObject = new Link(link.getId(), link.getActive(), vehicleObject, link.getInitialDate());

                linkedVehicles.add(linkObject);

            }

            //armo usuario extranjero.
            ForeignUser user = new ForeignUser(linkedVehicles, customer, userDTO.getCi(), userDTO.getName(),
                    userDTO.getPassword(), userDTO.getEmail(), userDTO.getId());

            userSerivce.registerUser(user);

            //TODO TIRAR evento y try and catch

        }
        //userService
    }

    @Override
    public void linkVehicle(Long id, VehicleDTO vehicleDTO) throws IllegalArgumentException, NoCustomerException {

        List<TollPass> listTollPass = new ArrayList<>();

        List<TollPassDTO> listTollPassDTO = vehicleDTO.getTollPassDTO();

        TollPass tollPassObject = null;

        Vehicle vehicle = null;

        for (TollPassDTO tollPassDTO : listTollPassDTO){

            tollPassObject = new TollPass(tollPassDTO.getId(),
                    tollPassDTO.getDate(), tollPassDTO.getCost(),
                    tollPassDTO.getPaymentType());
            listTollPass.add(tollPassObject);

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


        vehicleService.linkVehicle(id, vehicle);


    }

    @Override
    public void unLinkVehicle(Long id, VehicleDTO vehicleDTO) throws IllegalArgumentException, InvalidVehicleException, NoCustomerException {
    }

    @Override
    public Optional<List<VehicleDTO>> showLinkedVehicles(Long id) throws IllegalArgumentException, NoCustomerException {
        return Optional.empty();
    }

    @Override
    public void loadBalance(Long id, Double balance) throws IllegalArgumentException, NoCustomerException {

        try{
            accountService.loadBalance(id, balance);
        }catch (Exception ignored){
            //TODO exception
        }

    }

    @Override
    public Optional<Double> showBalance(Long id) throws IllegalArgumentException, NoCustomerException {

        try{
            return accountService.showBalance(id);
        }catch (Exception ignored){
            //TODO exception
        }
        return Optional.empty();
    }

    @Override
    public void linkCreditCard(Long id, CreditCardDTO creditCard) throws IllegalArgumentException, NoCustomerException {

        CreditCard card = null;
        if(creditCard != null){
            card = new CreditCard(creditCard.getId(), creditCard.getCardNumber(),
                    creditCard.getName(), creditCard.getExpireDate());
        }
        try{
            accountService.linkCreditCard(id, card);
        } catch (Exception ignored){
            //TODO exception
        }

    }

    @Override
    public Optional<List<TollPassDTO>> showPastPassages(Long id, LocalDate from, LocalDate to) throws IllegalArgumentException, IllegalRangeException, NoCustomerException {
        return Optional.empty();
    }

    @Override
    public Optional<List<TollPassDTO>> showPastPassagesVehicle(TagDTO tag, LocalDate from, LocalDate to) throws IllegalArgumentException, IllegalRangeException, NoCustomerException {
        return Optional.empty();
    }

    @Override
    public Optional<List<AccountDTO>> getAccountByTag(TagDTO tagDTO) throws IllegalArgumentException {

        Tag tag = new Tag(tagDTO.getUniqueId());

        //TODO try and catch
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

                    CreditCardDTO card = new CreditCardDTO(cardId, name, number, expireDate);

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
    public void prePay(Double balance, TagDTO tagDTO) throws IllegalArgumentException, NoCustomerException {

        Tag tag = new Tag(tagDTO.getUniqueId());

        try{

            accountService.prePay(tag, balance);

        }catch (Exception ignored){

        }

    }

    @Override
    public void postPay(Double balance, TagDTO tagDTO) throws IllegalArgumentException, NoCustomerException {

        Tag tag = new Tag(tagDTO.getUniqueId());
        try{
            accountService.postPay(tag, balance);

        }catch(Exception ignored){

        }

    }
}
