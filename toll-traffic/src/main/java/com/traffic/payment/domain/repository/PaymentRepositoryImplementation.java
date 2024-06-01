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
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PaymentRepositoryImplementation implements PaymentRepository {

    @PersistenceContext
    EntityManager session;
    EntityManager em;

    @PostConstruct
    public void initialize(){
        em = session.getEntityManagerFactory().createEntityManager();
    }

    public void addUser(User userToAdd){
        //users.add(userToAdd);
        System.out.println(userToAdd.getName());
        em.persist(userToAdd);
    }

    public List<User> getAllUsers() {
        //I call the criteria builder, which is the responsible for managing the queries
        CriteriaBuilder cb = em.getCriteriaBuilder();

        //I tell Criteria builder to start a query for User
        CriteriaQuery<User> cq = cb.createQuery(User.class);

        //I tell which user class I am asking for
        Root<User> root = cq.from(User.class);

        //perform a SELECT * FROM with the class I told you (user)
        cq.select(root);

        //I finally, tell the Entity Manager to return me the result from the query
        //that the criteria builder, built
        return em.createQuery(cq).getResultList();
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
            throw new IllegalArgumentException("No se pudo actualizar las pasadas del usuario " + userDTO.getName());
        }
    }
}
