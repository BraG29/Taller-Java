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
import com.traffic.dtos.PaymentTypeData;
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
import com.traffic.exceptions.NoAccountException;
import com.traffic.exceptions.NoCustomerException;
import com.traffic.monitoring.Interface.MonitoringController;
import com.traffic.payment.Interface.PaymentController;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class AccountServiceImpl implements AccountService {

    @Inject
    private ClientModuleRepository repo;

    @Inject
    private PaymentController paymentController;

    @Inject
    private CommunicationController communicationController;



    @Override
    public Boolean prePay(Tag tag, Double cost) throws NoAccountException,  NoCustomerException {

        if(cost <= 0){
            throw new IllegalArgumentException("El costo no puede ser 0 o menor");
        }

        Optional<User> usrOPT = repo.findByTag(tag);
        
        UserDTO userDTO = null;

        if(usrOPT.isPresent()){

            User usr = usrOPT.get();

            if(usr.getTollCustomer() != null){

                PREPay prePay = null;

                if(usr.getTollCustomer().getPrePay() != null){

                    prePay = usr.getTollCustomer().getPrePay();

                    if(cost > prePay.getBalance()){

                        //TODO: lanzar evento no llamar a la funcion
//                        monitoringController.notifyNotEnoughBalance();

                        //lanzo notificacion armo UserDTO con cosas basicas.
                        if(usr instanceof NationalUser){
                            userDTO = new NationalUserDTO(usr.getId(), usr.getEmail(), usr.getPassword(),
                                    usr.getName(), usr.getCi(), null, null, null,
                                    null);

                        }else if (usr instanceof ForeignUser){
                            userDTO = new ForeignUserDTO(usr.getId(), usr.getEmail(), usr.getPassword(),
                                    usr.getName(), usr.getCi(), null, null, null);
                        }

                        //llamo oper modulo comunicacion.
                        communicationController.notifyNotEnoughBalance(userDTO);

                        throw new IllegalArgumentException("Saldo insuficiente");
                        //return false;

                    }else{

                        //TODO evento de pago.
                        TollPass newPass = new TollPass(LocalDate.now(), cost, PaymentTypeData.PRE_PAYMENT);

                        for(Link link : usr.getLinkedCars()){
                            Vehicle vehicle = link.getVehicle();
                            if(vehicle.getTag().equals(tag)){
                                vehicle.addPass(newPass);
                            }
                        }

                        usr.getTollCustomer().getPrePay().pay(cost);
                        repo.update(usr);
                        return true;
                    }

                }else {
                    throw new NoAccountException("El usuario no tiene cuenta prepaga");
                }

            }
            return false;
        }

        return false;
    }

    @Override
    public Boolean postPay(Tag tag, Double cost) throws NoAccountException, NoCustomerException, ExternalApiException, InvalidVehicleException {

        Optional<User> usrOPT = repo.findByTag(tag);

        UserDTO userDTO = null;

        if(usrOPT.isPresent()){

            User usr = usrOPT.get();
            if(usr.getTollCustomer() != null){

                if(usr.getTollCustomer().getPostPay() == null){

                    throw new NoAccountException("El usuario: " + usr.getId() + "No tiene cuenta postPaga");
                }

                //Armo el customerDTO utilizando una funcion auxiliar.
                TollCustomerDTO customerDTO = getTollCustomerDTO(usr);

                //armo la lista de vinculos, armo el auto, sus pasadas, su placa si tiene, etc.
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
                try{
                    //en  este bloque se arma la lista de vinculos.
                    for (Link link : vehicles) {
                        if (link.getVehicle().getTag().getTagId().equals(tag.getTagId())) { //Si el vehiculo esta en la lista

                            vehicle = link.getVehicle();

                            //agrego nueva pasada al vehiculo, asi le mando datos actualizados al otro modulo.
                            TollPass newPass = new TollPass(LocalDate.now(), cost, PaymentTypeData.POST_PAYMENT);
                            vehicle.addPass(newPass);

                            listTollPass = vehicle.getTollPass();

                            for (TollPass tollPass : listTollPass){ //obtengo pasadas del vehiculo, y las paso a una listaDTO.
                                tollPassObject = new TollPassDTO(tollPass.getPassDate(), tollPass.getCost(), tollPass.getPaymentType());
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
                            linkListDTO.add(linkObject); //obtengo como resultado final una lista de linkDTO.
                        }
                    }

                    //en este bloque se arman los usuarios.
                    if(usr instanceof NationalUser){
                        userDTO = new NationalUserDTO(usr.getId(), usr.getEmail(), usr.getPassword(), usr.getName(),
                                usr.getCi(), customerDTO, linkListDTO, null, null);//Es necesario pasar en el usuario sucive y notificaciones?
                    } else if(usr instanceof  ForeignUser){
                        userDTO = new ForeignUserDTO(usr.getId(), usr.getEmail(), usr.getPassword(), usr.getName(),
                                usr.getCi(), customerDTO, linkListDTO, null);
                    }

                    paymentController.notifyPayment(userDTO, vehicleDTO, cost, customerDTO.getPostPayDTO().getCreditCardDTO());

                    repo.update(usr);//actualizo lista de usuario futura bd
                    return true;
                }catch (Exception e){
                    System.out.println("Ocurrio un error al realizar el pago" + e.getMessage());
                }
            }
            return false;
        }
        return false;
    }

    @Override
    public Optional<List<Account>> getAccountByTag(Tag tag) {

        Optional<User> usrOPT = repo.findByTag(tag);

        if(usrOPT.isPresent()){

            User usr = usrOPT.get();

            if(usr.getTollCustomer() != null){//si es cliente
                List<Account> accounts = new ArrayList<Account>();

                if(usr.getTollCustomer().getPrePay() != null){//le agrego una cuenta post paga si tiene.
                    accounts.add(usr.getTollCustomer().getPrePay());
                }

                if(usr.getTollCustomer().getPostPay() != null){ //agrego postpaga, si tiene.
                    accounts.add(usr.getTollCustomer().getPostPay());
                }

                return Optional.of(accounts);//devuelvo cuentas

            } else{
                return Optional.empty();
            }
        }

        return Optional.empty();

    }

    @Override
    public void loadBalance(Long id, Double balance) throws NoCustomerException {

        Optional<User> usrOPT = repo.getUserById(id);

        if(usrOPT.isPresent()){

            User usr = usrOPT.get();

            Integer accountNumber = PREPay.generateRandomAccountNumber();

            if (usr.getTollCustomer() == null) { //si no es cliente creo el objeto
                throw new NoCustomerException();

            } else if(usr.getTollCustomer().getPrePay() == null){ //si es cliente pero no tiene cuenta.
                usr.getTollCustomer().setPrePay(new PREPay(usr.getId(), accountNumber, LocalDate.now(), balance));

            } else { //si es cliente y tiene cuenta.
                usr.getTollCustomer().getPrePay().loadBalance(balance);
            }

            repo.update(usr);
        }
    }

    @Override
    public Optional<Double> showBalance(Long id) {

        Optional<User> usrOPT = repo.getUserById(id);

        if(usrOPT.isPresent()){

            User usr = usrOPT.get();
            if (usr.getTollCustomer() != null && usr.getTollCustomer().getPrePay() != null){

                return Optional.of(usr.getTollCustomer().getPrePay().getBalance());
            }
        }

        return Optional.empty();
    }

    @Override
    public void linkCreditCard(Long id, CreditCard creditCard) {

        Optional<User> usrOPT = repo.getUserById(id);

        if(usrOPT.isPresent()){

            User usr = usrOPT.get();

            POSTPay postPay;

            LocalDate creationDate = LocalDate.now();

            //nunca deberia cumplirse que no es cliente ya que solo la llamaran clientes a esta oper.
            if (usr.getTollCustomer().getPostPay() == null){ //si no tiene cuenta postPaga le creo una y le agrego la tarjeta.

                Integer accountNumber = POSTPay.generateRandomAccountNumber();

                postPay = new POSTPay(id,accountNumber, creationDate, creditCard);

                usr.getTollCustomer().setPostPay(postPay);

            }else{ //si ya tiene cuenta postpaga, cambio la tarjeta, por ahora maneja una tarjeta sola.
                //usr.addCreditCard(creditCard);
                usr.getTollCustomer().getPostPay().setCreditCard(creditCard);
            }
            repo.update(usr);
        }
    }


    //auxiliares:

    /**
     * Función auxiliar encargada de realizar un pasaje de objeto  TollCustomer a TollCustomerDTO.
     *
     * @param usr -> recibe un usr, concramente utilizará el objeto TollCustomer de este.
     * @return -> retorna como resultado el armado del objeto TollCustomerDTO
     */
    private static TollCustomerDTO getTollCustomerDTO(User usr) {
        PREPay prePay = null;
        PrePayDTO prePayDTO = null;

        POSTPay postPay = null;
        PostPayDTO postPayDTO = null;

        CreditCard card = null;
        CreditCardDTO cardDTO = null;

        //armo cuenta postpay si tiene
        if(usr.getTollCustomer().getPostPay() != null){

            card = usr.getTollCustomer().getPostPay().getCreditCard();
            cardDTO = new CreditCardDTO(card.getId(), card.getCardNumber(), card.getName(), card.getExpireDate());

            postPay = usr.getTollCustomer().getPostPay();
            postPayDTO = new PostPayDTO(postPay.getId(), postPay.getAccountNumber(), postPay.getCreationDate(), cardDTO);

        }

        //armo cuenta prepay si tiene
        if(usr.getTollCustomer().getPrePay() != null){
            prePay = usr.getTollCustomer().getPrePay();
            prePayDTO = new PrePayDTO(prePay.getId(), prePay.getAccountNumber(), prePay.getCreationDate(), prePay.getBalance());
        }

        return new TollCustomerDTO(usr.getTollCustomer().getId(), postPayDTO, prePayDTO);
    }
    
}
