package com.traffic.toll.application.api;

import com.traffic.toll.domain.entities.LicensePlate;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.awt.*;

@Path("/toll")
@ApplicationScoped
public class TollRestController {

//    @PersistenceUnit
//    EntityManagerFactory entityManagerFactory;
    @PersistenceContext
    EntityManager entityManager;

//    @PostConstruct
//    public void setUp(){
//        entityManager = entityManagerFactory.createEntityManager();
//    }

    //curl -v http://0.0.0.0:8081/toll-traffic/api/toll/hello
    @GET
    @Produces("text/plain")
    @Path("/hello")
    @Transactional
    public String hello() {

        LicensePlate licensePlate = new LicensePlate(null, "ABC-123");

        try(EntityManager em = entityManager.getEntityManagerFactory().createEntityManager()){
//            entityManager.getTransaction().begin();
            em.persist(licensePlate);
            em.flush();
//            em.getTransaction().commit();

        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }

        return "Entity Manager class type: " + entityManager.getClass();
    }

    //curl -v http://0.0.0.0:8081/toll-traffic/api/toll/licensePlate/1
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("licensePlate/{id}")
    public Response getLicensePlate(@PathParam("id") Long id){
        return Response.ok(entityManager.find(LicensePlate.class, id)).build();
    }
}