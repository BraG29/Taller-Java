package com.traffic.client.domain.User;

import com.traffic.client.domain.Account.CreditCard;
import com.traffic.client.domain.Vehicle.Link;
import com.traffic.client.domain.Vehicle.Vehicle;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public abstract class User {

    private Long id;
    private String email;
    private String password;
    private String name;
    private String ci;
    private TollCustomer tollCustomer;
    private List<Link> linkedCars;

    public User(){}

    public User(List<Link> linkedCars, TollCustomer tollCustomer, String ci, String name, String password, String email, Long id) {
        this.linkedCars = linkedCars;
        this.tollCustomer = tollCustomer;
        this.ci = ci;
        this.name = name;
        this.password = password;
        this.email = email;
        this.id = id;
    }

    public void addCreditCard(CreditCard card){
        tollCustomer.getPostPay().setCreditCard(card);
    }

    public void addVehicle(Vehicle vehicle){
        if(linkedCars == null){
            linkedCars = new ArrayList<>();
            Link link = new Link(this.getId(), true, vehicle, LocalDate.now());
            linkedCars.add(link);

        } else {

            Long id = linkedCars.size() + 1L; //el id se lo doy utilizando el tamaño de  la lista +1
            //esto en el futuro se ignora ya que la bd deberia brindarles ids incrementales para que no se repitan.

            Link link = new Link(id, true, vehicle, LocalDate.now());
            linkedCars.add(link);
            }
    }

    public void removeVehicle(Vehicle vehicle){
        if(linkedCars != null){

            for (Link link : linkedCars){

                Long id = vehicle.getTag().getTagId();

                if(link.getVehicle().getTag().getTagId().equals(id)){
                    linkedCars.remove(link);
                }
            }
        }
    }

}
