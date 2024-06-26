package com.traffic.payment.domain.repository;

import com.traffic.dtos.PaymentTypeData;
import com.traffic.dtos.account.CreditCardDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.dtos.vehicle.TagDTO;
import com.traffic.dtos.vehicle.VehicleDTO;
import com.traffic.events.CreditCardPaymentEvent;
import com.traffic.exceptions.InternalErrorException;
import com.traffic.payment.domain.entities.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class PaymentRepositoryImplementation implements PaymentRepository {

    //I inject the event I will eventually publish
    @Inject
    private Event<CreditCardPaymentEvent> CreditCardPaymentPublisher;

    @PersistenceContext
    EntityManager session;
    EntityManager em;

    @Override
    @Transactional
    public Optional<Vehicle> addVehicle(Vehicle vehicleToAdd) throws Exception {

        vehicleToAdd.setTag(em.merge(vehicleToAdd.getTag()));
       // vehicleToAdd.setPlate(em.merge(vehicleToAdd.getPlate()));

        try {
            Vehicle vehicle = em.merge(vehicleToAdd);
            em.flush();
            return Optional.of(vehicle);

        } catch (Exception e) {
            System.err.println("No se pudo dar de alta vehiculo para POSTPago: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public void saveLink(Link link) {
        em.persist(link);
        em.flush();
    }

    @PostConstruct
    public void initialize(){
        em = session.getEntityManagerFactory().createEntityManager();
    }


    @Transactional
    @Override
    public void addUser(User userToAdd){
        System.out.println(userToAdd.getName());
        session.persist(userToAdd);

        session.flush();
    }

    @Override
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

    @Override
    public void addTollPassToUserVehicle(UserDTO userDTO,
                                         VehicleDTO vehicleDTO,
                                         Double amount,
                                         CreditCardDTO creditCardDTO) throws InternalErrorException {

        //kinda useless thing to do, I don't know why I implemented this
        //User userToAdd = getUserById(userDTO.getId());

        try {
            //we find the vehicle we will be updating the toll passes
            Vehicle vehicleToUpdate = findVehicleByTag(vehicleDTO.getTagDTO());

            //we create the toll pass to add
            TollPass tollPassToAdd = new TollPass(null, LocalDate.now(),amount, PaymentTypeData.POST_PAYMENT);

            //we set the toll pass to the previously gotten vehicle
            tollPassToAdd.setVehicle(vehicleToUpdate);

            //persist
            em.persist(tollPassToAdd);

            em.flush(); //we flush

            //we fire the event that informs the rest of the modules that a payment has ocurred
            CreditCardPaymentEvent creditCardPayment = new CreditCardPaymentEvent("Se realizó el pago con tarjeta de crédito para el vehiculo con TAG: " + vehicleToUpdate.getTag());
            CreditCardPaymentPublisher.fire(creditCardPayment);

        }catch (Exception e){
            throw new IllegalArgumentException("No se pudo actualizar las pasadas del usuario " + userDTO.getName() + e.getMessage() ) ;
        }
    }

    @Override
    public List<TollPass> getAllTollPasses() {

        //I call the criteria builder, which is the responsible for managing the queries
        CriteriaBuilder cb = em.getCriteriaBuilder();

        //I tell Criteria builder to start a query for Toll Pass
        CriteriaQuery<TollPass> cq = cb.createQuery(TollPass.class);

        //I tell which user class I am asking for
        Root<TollPass> root = cq.from(TollPass.class);

        //perform a SELECT * FROM with the class I told you (TollPass)
        cq.select(root);

        //I finally, tell the Entity Manager to return me the result from the query
        //that the criteria builder, built
        return em.createQuery(cq).getResultList();
    }


    @Override
    public Vehicle findVehicleByTag(TagDTO tagDTO) throws InternalErrorException {
        try {
            //I get the Tag domain object from the vehicle I want to find
            Tag tag = em.find(Tag.class, tagDTO.getId());

            //I get Criteria Builder
            CriteriaBuilder cBuilder = em.getCriteriaBuilder();

            //I get a Criteria Query for Vehicle
            CriteriaQuery<Vehicle> cQuery = cBuilder.createQuery(Vehicle.class);

            //I get a root for Vehicle
            Root<Vehicle> root = cQuery.from(Vehicle.class);
            //credit card payment

            //SELECT * FROM Payment_Vehicle WHERE TagID = tagDTO
//            cQuery.select(root).where(cBuilder.equal(root.get("tag").get("uniqueId"), UUID.fromString(tagDTO.getUniqueId())));
            cQuery.select(root).where(cBuilder.equal(root.get("tag"), tag));

            return em.createQuery(cQuery).getSingleResult();
        }
        catch (Exception e){
            throw new InternalErrorException(" no se pudo encontrar el vehiculo para cobrar");
        }
    }
}
