package com.traffic.toll.Interface.api;

import com.traffic.dtos.vehicle.IdentifierDTO;
import com.traffic.exceptions.InvalidVehicleException;
import com.traffic.toll.Interface.local.TollService;
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
@Transactional
public class TollController {

    @Inject
    private TollService tollService;

    @POST
    @Path("/isEnable")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response isEnable(IdentifierDTO identifier) {
        Boolean responseValue = false;

        try {
            responseValue = tollService.isEnabled(identifier).orElseThrow();

        } catch (IllegalArgumentException | InvalidVehicleException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Json.createObjectBuilder()
                            .add("error", e.getMessage()))
                    .build();

        } catch (NoSuchElementException e) {
            return Response.serverError().build();
        }

        return Response.ok(responseValue).build();
    }

    @GET
    @Path("/setCommon/{amount}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCommonTariff(@PathParam("amount") double amount){
        try{
            tollService.updateCommonTariff(amount);

        } catch (IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Json.createObjectBuilder()
                            .add("error", e.getMessage()))
                    .build();

        } catch (NoSuchElementException | PersistenceException e){
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