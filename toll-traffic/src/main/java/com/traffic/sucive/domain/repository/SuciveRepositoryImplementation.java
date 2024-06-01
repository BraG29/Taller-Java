package com.traffic.sucive.domain.repository;

import com.traffic.dtos.PaymentTypeData;
import com.traffic.dtos.vehicle.LicensePlateDTO;
import com.traffic.exceptions.InvalidVehicleException;
import com.traffic.sucive.domain.entities.NationalVehicle;
//import com.traffic.sucive.domain.user.User;
import com.traffic.sucive.domain.entities.TollPass;
import com.traffic.sucive.domain.entities.User;
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
public class SuciveRepositoryImplementation  implements SuciveRepository {

    private ArrayList<User> users;

    @PersistenceContext
    EntityManager session;
    EntityManager em;



    @PostConstruct
    public void initialize(){
        users = new ArrayList<>();
        em = session.getEntityManagerFactory().createEntityManager();

    }

    @Override
    public void addUser(User user){
        users.add(user);
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

    public NationalVehicle findVehicleByLicensePlate(LicensePlateDTO licensePlateDTO){

        return em.find(NationalVehicle.class, licensePlateDTO.getLicensePlateNumber());

    }

    @Override
    public void updateVehicleTollPass(LicensePlateDTO licensePlateDTO, Double amount) throws InvalidVehicleException {
        //I find the national vehicle I will be updating
        NationalVehicle vehicleToUpdate = findVehicleByLicensePlate(licensePlateDTO);

        if (vehicleToUpdate != null) {
            //I create the toll pass that I am going to add
            TollPass tollPassToAdd = new TollPass(null,LocalDate.now(),amount, PaymentTypeData.SUCIVE );

            em.persist(vehicleToUpdate //we persist the vehicle
                    .getTollPass() //we get the toll passes from the vehicle
                    .add(em.merge(tollPassToAdd)));//we add and persist the toll pass we created
            em.flush(); //we flush
        }
      else{
          throw new InvalidVehicleException("No se encontró el vehiculo");
        }

    }
}
