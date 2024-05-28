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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


//Implementar metodos de comunicacion con bd
@ApplicationScoped
public class ClientModuleRepositoryImpl implements ClientModuleRepository{

    List<User> usersList;
    List<Link> nationalVehicles;
    List<Link> foreignVehicles;
    List<TollPass> passList;


    @PostConstruct
    public void usersInit(){
        
        this.usersList = new ArrayList<>();
        this.nationalVehicles = new ArrayList<>();
        this.foreignVehicles = new ArrayList<>();
        this.passList = new ArrayList<>();

        passList.add(new TollPass(0L, LocalDate.now(),200D, PaymentTypeData.PRE_PAYMENT));
        passList.add(new TollPass(1L, LocalDate.now(),2300D, PaymentTypeData.POST_PAYMENT));
        passList.add(new TollPass(2L, LocalDate.now(),5500D, PaymentTypeData.SUCIVE));


        TollCustomer customer = new TollCustomer(1L, new POSTPay(1L, 3231, LocalDate.now(),
                new CreditCard(1L,"1234.1561.1236.6236" ,"Pepe" ,LocalDate.now())),
                new PREPay(1L, 1231235, LocalDate.now(), 2000D));

        /*******************************/
        Tag tagNational = new Tag(203123L);

        LicensePlate plate = new LicensePlate("MAG 2013");

        Vehicle nationalVehicle = new NationalVehicle(2L, tagNational, null, plate);

        //vehiculo2

        Tag tagNational2 = new Tag(253897L);

        LicensePlate plate2 = new LicensePlate("BTC2000");

        Vehicle nationalVehicle2 = new NationalVehicle(5L, tagNational2, passList, plate2);

        Link nationalLink = new Link(0L, true, nationalVehicle, LocalDate.now());
        Link nationalLink2 = new Link(1L, true, nationalVehicle2, LocalDate.now());

        nationalVehicles.add(nationalLink);
        nationalVehicles.add(nationalLink2);

        User nationalUser = new NationalUser(nationalVehicles, customer, "5.123.156-2",
                "Pepe", "1234", "pepe@mail.com", 1L, null);

        /***********************************************************/

        Tag tagForeign = new Tag(123512L);

        Vehicle foreignVehicle = new ForeignVehicle(2L, tagForeign, passList);

        Link foreignLink = new Link(1L, true, foreignVehicle, LocalDate.now());
        foreignVehicles.add(foreignLink);
        
        User foreignUser = new ForeignUser(foreignVehicles, customer, "1.234.512-2",
                "Felipe", "1234", "Felipe@mail.com", 2L);


        /********************/
        TollCustomer customerTest = new TollCustomer(51L, null, null);
        User userTestRepo = new NationalUser(null, customerTest, "5.123.636-8","Matias",
                "1234", "matias@mail.com" ,51L, null) ;


        usersList.add(nationalUser);
        usersList.add(foreignUser);
        usersList.add(userTestRepo);

    }



    public void update(User  usr){
        //comprobar actualizar lista de usersList
        for(int i = 0; i < this.usersList.size(); i++){
            if(usr.getId().equals(this.usersList.get(i).getId())){
                this.usersList.set(i, usr);
            }
        }
    }

    @Override
    public Optional<User> findByTag(Tag tag) {

        for (User user : this.usersList){
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

        if(this.usersList != null){

            return Optional.of(new ArrayList<>(this.usersList));
        }

        return Optional.empty();
    }

    @Override
    public void createUser(User user) {
        usersList.add(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {

        for (User user : this.usersList){
            if(Objects.equals(user.getId(), id)){//vamo a probar solucion intellij
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<Vehicle> getVehicleByTag(Tag tag) {
        List<Link> links = new ArrayList<>();

        for(User users : this.usersList){

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
