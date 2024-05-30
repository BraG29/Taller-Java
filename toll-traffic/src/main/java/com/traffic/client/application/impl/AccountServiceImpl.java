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
import com.traffic.exceptions.NoAccountException;
import com.traffic.exceptions.NoCustomerException;
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
    public void prePay(Tag tag, Double cost) throws Exception {

        if(cost <= 0){
            throw new IllegalArgumentException("El costo no puede ser menor o igual a 0");
        }

        repo.prePay(tag.getId(), cost);
    }

    @Override
    public void postPay(Tag tag, Double cost) throws Exception {
        if(tag == null){
            throw new NoCustomerException("El tag es vacio.");
        }

        repo.postPay(tag.getId(), cost);

    }

    @Override
    public Optional<List<Account>> getAccountByTag(Tag tag) {
        return repo.getAccountsByTag(tag.getId());
    }

    @Override
    public void loadBalance(Long id, Double balance) throws Exception {

       repo.loadBalance(id, balance);
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


    //auxiliar:

    /**
     * Función auxiliar encargada de realizar un pasaje de objeto  TollCustomer a TollCustomerDTO.
     *
     * @param usr -> recibe un usr, concramente utilizará el objeto TollCustomer de este.
     * @return -> retorna como resultado el armado del objeto TollCustomerDTO
     */
    private static TollCustomerDTO getTollCustomerDTO(User usr) {
        PREPay prePay;
        PrePayDTO prePayDTO = null;

        POSTPay postPay;
        PostPayDTO postPayDTO = null;

        CreditCard card;
        CreditCardDTO cardDTO;

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
