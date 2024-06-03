package com.traffic.payment.domain.repository;


import com.traffic.dtos.PaymentTypeData;
import com.traffic.dtos.account.CreditCardDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.dtos.vehicle.TagDTO;
import com.traffic.dtos.vehicle.VehicleDTO;
import com.traffic.exceptions.InternalErrorException;
import com.traffic.payment.domain.entities.Tag;
import com.traffic.payment.domain.entities.TollPass;
import com.traffic.payment.domain.entities.User;
import com.traffic.payment.domain.entities.Vehicle;
import com.traffic.sucive.domain.entities.LicensePlate;
import com.traffic.sucive.domain.entities.NationalVehicle;
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

    public void addTollPassToUserVehicle(UserDTO userDTO, VehicleDTO vehicleDTO, Double amount, CreditCardDTO creditCardDTO) throws InternalErrorException {

        //kinda useless thing to do, I don't know why I implemented this
        //User userToAdd = getUserById(userDTO.getId());

        try {
            Vehicle vehicleToUpdate = findVehicleByTag(vehicleDTO.getTagDTO());
            TollPass tollPassToAdd = new TollPass(null, LocalDate.now(),amount, PaymentTypeData.POST_PAYMENT);

            em.persist(vehicleToUpdate //we persist the vehicle
                    .getTollPass() //we get the toll passes from the vehicle
                    .add(em.merge(tollPassToAdd)));//we add and persist the toll pass we created
            em.flush(); //we flush

        }catch (Exception e){
            throw new IllegalArgumentException("No se pudo actualizar las pasadas del usuario " + userDTO.getName());
        }
    }

    public Vehicle findVehicleByTag(TagDTO tagDTO) throws InternalErrorException {
        try {
            //I get the Tag domain object from the vehicle I want to find
            Tag license = em.find(Tag.class, tagDTO.getId());

            //I get Criteria Builder
            CriteriaBuilder cBuilder = em.getCriteriaBuilder();

            //I get a Criteria Query for Vehicle
            CriteriaQuery<Vehicle> cQuery = cBuilder.createQuery(Vehicle.class);

            //I get a root for Vehicle
            Root<Vehicle> root = cQuery.from(Vehicle.class);

            //SELECT * FROM Payment_Vehicle WHERE TagID = tagDTO
            cQuery.select(root).where(cBuilder.equal(root.get("Tag_id"), license));

            return em.createQuery(cQuery).getSingleResult();
        }
        catch (Exception e){
            throw new InternalErrorException("no se pudo encontrar el vehiculo nacional para cobrar Sucive");
        }
    }
}
