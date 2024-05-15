package com.traffic.client.domain.repository;

import com.traffic.client.domain.Account.CreditCard;
import com.traffic.client.domain.Account.POSTPay;
import com.traffic.client.domain.Account.PREPay;
import com.traffic.client.domain.User.ForeignUser;
import com.traffic.client.domain.User.NationalUser;
import com.traffic.client.domain.User.TollCustomer;
import com.traffic.client.domain.User.User;
import com.traffic.client.domain.Vehicle.*;
import com.traffic.dtos.PaymentTypeData;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.jdeparser.JArrayExpr;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


//Implementar metodos de comunicacion con bd
@ApplicationScoped
public class ClientModuleRepositoryImpl implements ClientModuleRepository{


    List<User> usuarios;
    List<Link> vehiculosNacional;
    List<Link> vehiculosExtranjero;
    List<TollPass> pasadas;


    @PostConstruct
    public void usersInit(){
        
        this.usuarios = new ArrayList<>();
        this.vehiculosNacional = new ArrayList<>();
        this.vehiculosExtranjero = new ArrayList<>();
        this.pasadas = new ArrayList<>();

        pasadas.add(new TollPass(0L, LocalDate.now(),200D, PaymentTypeData.PRE_PAYMENT));
        pasadas.add(new TollPass(1L, LocalDate.now(),2300D, PaymentTypeData.POST_PAYMENT));
        pasadas.add(new TollPass(2L, LocalDate.now(),5500D, PaymentTypeData.SUCIVE));

        TollCustomer customer = new TollCustomer(1L, new POSTPay(1L, 3231, LocalDate.now(),
                new CreditCard(1L,"1234.1561.1236.6236" ,"Pepe" ,LocalDate.now())),
                new PREPay(1L, 1231235, LocalDate.now(), 2000D));

        /*******************************/
        Tag tagNational = new Tag(203123L);

        LicensePlate plate = new LicensePlate("MAG 2013");

        Vehicle nationalVehicle = new NationalVehicle(2L, tagNational, pasadas, plate);

        Link nationalLink = new Link(0L, true, nationalVehicle, LocalDate.now());
        vehiculosNacional.add(nationalLink);

        User nationalUser = new NationalUser(vehiculosNacional, customer, "5.123.156-2",
                "Pepe", "1234", "pepe@mail.com", 1L, null);

        /***********************************************************/

        Tag tagForeign = new Tag(123512L);

        Vehicle foreignVehicle = new ForeignVehicle(2L, tagForeign, pasadas);

        Link foreignLink = new Link(1L, true, foreignVehicle, LocalDate.now());
        vehiculosExtranjero.add(foreignLink);
        
        User foreignUser = new ForeignUser(vehiculosExtranjero, customer, "1.234.512-2",
                "Felipe", "1234", "Felipe@mail.com", 2L);
        
        usuarios.add(nationalUser);
        usuarios.add(foreignUser);

    }

    @Override
    public void linkVehicle(User usr, Vehicle vehicle) {
        usr.addVehicle(vehicle);
    }

    @Override
    public void unLinkVehicle(User usr, Vehicle vehicle) {
        usr.removeVehicle(vehicle);
    }

    @Override
    public Optional<List<Vehicle>> showLinkedVehicles(User usr) {

        List<Vehicle> vehicles = new ArrayList<>();

        for(Link link : usr.getLinkedCars()){
            vehicles.add(link.getVehicle());
        }

        return Optional.of(vehicles);
    }

    @Override
    public void linkCreditCard(User usr, CreditCard card) {
            usr.getTollCustomer().getPostPay().setCreditCard(card);
    }

    @Override
    public void loadBalance(User usr, Double balance) {
        usr.getTollCustomer().getPrePay().loadBalance(balance);
    }

    @Override
    public Optional<Double> showBalance(User usr) {
        return Optional.of(usr.getTollCustomer().getPrePay().getBalance());
    }

    @Override
    public void payPrePay(User usr, Double balance) {
        usr.getTollCustomer().getPrePay().pay(balance);
    }

    @Override
    public Optional<User> findByTag(Tag tag) {

        for (User user : this.usuarios){
            if (user.getLinkedCars() != null){
                for(Link link : user.getLinkedCars()){
                    if(link.getVehicle().getTag().equals(tag)){
                        return Optional.of(user);
                    }
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<List<User>> listUsers() {

        if(this.usuarios != null){

            return Optional.of(this.usuarios);
        }

        return Optional.empty();

    }

    @Override
    public void createUser(User user) {
        usuarios.add(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {

        for (User user : this.usuarios){
            if(Objects.equals(user.getId(), id)){//vamo a probar solucion intellij
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<Vehicle> getVehicleByTag(Tag tag) {
        List<Link> links = new ArrayList<>();

        for(User users : this.usuarios){

            links = users.getLinkedCars();

            for (Link link : links){
                if(link.getVehicle().getTag().equals(tag)){
                    Vehicle vehicle = link.getVehicle();
                    return Optional.of(vehicle);
                }
            }

        }

        return Optional.empty();
    }
}
