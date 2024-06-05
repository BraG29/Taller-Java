package com.traffic.sucive.domain.repository;

import com.traffic.dtos.PaymentTypeData;
import com.traffic.dtos.vehicle.LicensePlateDTO;
import com.traffic.exceptions.InternalErrorException;
import com.traffic.exceptions.InvalidVehicleException;
import com.traffic.sucive.domain.entities.LicensePlate;
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
import org.jboss.weld.interceptor.proxy.InterceptorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class SuciveRepositoryImplementation  implements SuciveRepository {

    //private ArrayList<User> users;

    @PersistenceContext
    EntityManager session;
    EntityManager em;



    @PostConstruct
    public void initialize(){
        em = session.getEntityManagerFactory().createEntityManager();

    }

    @Override
    public void addUser(User user){
            em.persist(user);
    }

    @Override
    public List<TollPass> getAllTollPasses() { //TODO change to getAllTollPasses DONETE! 👍

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

    public NationalVehicle findVehicleByLicensePlate(LicensePlateDTO licensePlateDTO) throws InternalErrorException {

        try {
            //I get the license plate domain object from the vehicle I want to find
            LicensePlate license = em.find(LicensePlate.class, licensePlateDTO.getLicensePlateNumber());

            //I get Criteria Builder
            CriteriaBuilder cBuilder = em.getCriteriaBuilder();

            //I get a Criteria Query for National Vehicle
            CriteriaQuery<NationalVehicle> cQuery = cBuilder.createQuery(NationalVehicle.class);

            //I get a root for National Vehicle
            Root<NationalVehicle> root = cQuery.from(NationalVehicle.class);

            //SELECT * FROM Sucive_NationalVehicle WHERE licensePlate = license
            cQuery.select(root).where(cBuilder.equal(root.get("plate"), license));

            return em.createQuery(cQuery).getSingleResult();
        }
        catch (Exception e){

            throw new InternalErrorException("no se pudo encontrar el vehiculo nacional con placa: "
                    + licensePlateDTO.getLicensePlateNumber()
                    + " para cobrar Sucive");
        }

    }

    @Override
    public void updateVehicleTollPass(LicensePlateDTO licensePlateDTO, Double amount) throws InvalidVehicleException, InternalErrorException {
        //I find the national vehicle I will be updating
        NationalVehicle vehicleToUpdate = findVehicleByLicensePlate(licensePlateDTO);

        try {
          //I create the toll pass that I am going to add
          TollPass tollPassToAdd = new TollPass(null,LocalDate.now(),amount, PaymentTypeData.SUCIVE);

          //we set the vehicle of the toll pass we created with the one we found
          tollPassToAdd.setVehicle(vehicleToUpdate);

          //we persist the data
          em.persist(tollPassToAdd);

            //we flush
          em.flush();
      }
      catch (Exception e){
          throw new InternalErrorException(e.getMessage());
      }
    }
}
