package com.traffic.payment.domain.repository;


import com.traffic.dtos.PaymentTypeData;
import com.traffic.dtos.account.CreditCardDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.dtos.vehicle.VehicleDTO;
import com.traffic.payment.domain.entities.TollPass;
import com.traffic.payment.domain.entities.User;
import com.traffic.payment.domain.entities.Vehicle;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDate;
import java.util.ArrayList;

@ApplicationScoped
public class PaymentRepositoryImplementation implements PaymentRepository {

    ArrayList<User> users;

    @PersistenceContext
    EntityManager session;
    EntityManager em;

    @PostConstruct
    public void initialize(){
        users = new ArrayList<>();
        //users.add(new User());
        em = session.getEntityManagerFactory().createEntityManager();
    }

    public void addUser(User userToAdd){
        users.add(userToAdd);
        System.out.println(userToAdd.getName());
    }

    public ArrayList<User> getAllUsers() {
        return users;
    }

    public User getUserById(Long id){
        return em.find(User.class, id);
    }

    public void addTollPassToUserVehicle(UserDTO userDTO, VehicleDTO vehicleDTO, Double amount, CreditCardDTO creditCardDTO){

        User userToAdd = getUserById(userDTO.getId());

        if (userToAdd != null){
            Vehicle vehicleToUpdate = em.find(Vehicle.class, vehicleDTO.getId());
            TollPass tollPassToAdd = new TollPass(null, LocalDate.now(),amount, PaymentTypeData.POST_PAYMENT);

            em.persist(vehicleToUpdate //we persist the vehicle
                    .getTollPass() //we get the toll passes from the vehicle
                    .add(em.merge(tollPassToAdd)));//we add and persist the toll pass we created
            em.flush(); //we flush
        }else {
            throw new IllegalArgumentException("No existe el usuario");
        }
    }
}
