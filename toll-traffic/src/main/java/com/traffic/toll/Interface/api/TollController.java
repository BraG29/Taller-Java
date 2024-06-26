package com.traffic.toll.Interface.api;

import com.traffic.dtos.vehicle.IdentifierDTO;
import com.traffic.exceptions.InternalErrorException;
import com.traffic.exceptions.InvalidVehicleException;
import com.traffic.toll.Interface.local.TollService;
import com.traffic.toll.domain.repositories.VehicleRepository;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.NoSuchElementException;

@Path("/toll")
@ApplicationScoped
//@Transactional
public class TollController {

    @Inject
    private TollService tollService;

//    @GET
//    @Path("/initdb")
//    public Response initializeDatabase() {
//        tollService.initVehicles();
//
//        return Response.ok().build();
//    }

    @POST
    @Path("/isEnable")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response isEnable(IdentifierDTO identifier) {
        Boolean responseValue = false;

        try {
            responseValue = tollService.isEnabled(identifier).orElseThrow();

        } catch (IllegalArgumentException | InvalidVehicleException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(Json
                            .createObjectBuilder()
                            .add("error", e.getMessage())
                            .build())
                    .build();

        } catch (InternalErrorException e) {
            return Response
                    .serverError()
                    .entity(Json.createObjectBuilder().add("error", e.getMessage()).build())
                    .build();
        }
        System.out.println("Saliendo por OK - 200");
        return Response
                .ok()
                .entity(Json.createObjectBuilder().add("enable", Boolean.toString(responseValue)).build())
                .build();
    }

    @GET
    @Path("/setCommon/{amount}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCommonTariff(@PathParam("amount") double amount){
        try{
            tollService.updateCommonTariff(amount);

        } catch (IllegalArgumentException e){
            System.err.printf("""
                    Error en %s: %s 
                    """, this.getClass(), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Json.createObjectBuilder()
                            .add("error", e.getMessage()))
                    .build();

        } catch (NoSuchElementException | PersistenceException e){
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.ok("La Tarifa comun tiene un nuevo precio").build();
    }

    @GET
    @Path("/setPreferential/{amount}")
    public Response updatePreferentialTariff(@PathParam("amount") Double amount){
        try{
            tollService.updatePreferentialTariff(amount);

        } catch (IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Json.createObjectBuilder()
                            .add("error", e.getMessage()))
                    .build();

        } catch (NoSuchElementException | PersistenceException e){
            return Response.serverError().build();
        }

        return Response.ok("La Tarifa preferencial tiene un nuevo precio").build();

    }
}