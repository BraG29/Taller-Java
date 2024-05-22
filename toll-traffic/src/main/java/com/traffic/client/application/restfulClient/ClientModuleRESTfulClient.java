package com.traffic.client.application.restfulClient;

import com.traffic.client.Interface.ClientController;
import com.traffic.client.domain.User.User;
import com.traffic.dtos.account.AccountDTO;
import com.traffic.dtos.account.CreditCardDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.dtos.vehicle.TagDTO;
import com.traffic.dtos.vehicle.TollPassDTO;
import com.traffic.dtos.vehicle.VehicleDTO;
import com.traffic.exceptions.IllegalRangeException;
import com.traffic.exceptions.InvalidVehicleException;
import com.traffic.exceptions.NoCustomerException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Path("/TollCustomer")
public class ClientModuleRESTfulClient {

    @Inject
    private ClientController controller;

    //Usuarios

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/add")
    public UserDTO addUser(UserDTO user) {
        //TODO controles
        controller.addTollCostumer(user);
        return user;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Optional<List<User>> getAllUsers(){
        return controller.listUsers();
    }

    //vehiculos

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/vehicle/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addVehicle(@PathParam("userId") Long id, VehicleDTO vehicle) throws NoCustomerException {
        //TODO controles
        controller.linkVehicle(id, vehicle);
        return  Response.status(Response.Status.CREATED).entity("El vehiculo fue añadido con exito" +
                        " al usuario con  id: " + id).build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/vehicle/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeVehicle(@PathParam("userId") Long id, VehicleDTO vehicle) throws NoCustomerException, InvalidVehicleException {
        //TODO controles
        controller.unLinkVehicle(id, vehicle);
        return  Response.status(Response.Status.CREATED).entity("El vehiculo fue eliminado con exito " +
                "del usuario con id: " + id).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/vehicle/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Optional<List<VehicleDTO>> getVehicles(@PathParam("userId") Long id) throws NoCustomerException {
        //TODO controles
        return  controller.showLinkedVehicles(id);
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/showPass/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Optional<List<TollPassDTO>> showPass(@PathParam("userId") Long id, @QueryParam("from") String from,
                                                @QueryParam("to") String to)
            throws NoCustomerException, IllegalRangeException {

        LocalDate fromDate = LocalDate.parse(from, DateTimeFormatter.ISO_DATE);
        LocalDate toDate = LocalDate.parse(to, DateTimeFormatter.ISO_DATE);

        //TODO controles
        return  controller.showPastPassages(id, fromDate, toDate);
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/showPassVehicle/{tag}")
    @Produces(MediaType.APPLICATION_JSON)
    public Optional<List<TollPassDTO>> showPassVehicle(@QueryParam("from") String from,
                                                @QueryParam("to") String to, @PathParam("tag") Long tag)
            throws NoCustomerException, IllegalRangeException {

        LocalDate fromDate = LocalDate.parse(from, DateTimeFormatter.ISO_DATE);
        LocalDate toDate = LocalDate.parse(to, DateTimeFormatter.ISO_DATE);

        TagDTO tagDTO = new TagDTO(tag);

        //TODO controles
        return  controller.showPastPassagesVehicle(tagDTO, fromDate, toDate);
    }

    //cuentas

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/getAccounts/{tag}")
    @Produces(MediaType.APPLICATION_JSON)
    public Optional<List<AccountDTO>> getAccountsByTag(@PathParam("tag") Long tag){
        TagDTO tagDTO = new TagDTO(tag);
        //TODO controles
        return controller.getAccountByTag(tagDTO);
    }

    @PUT
    @Path("/addBalance")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response loadBalance(@QueryParam("balance") Double balance, @QueryParam("userId") Long id) throws NoCustomerException {
        //TODO controles
        controller.loadBalance(id, balance);
        return  Response.status(Response.Status.CREATED).entity("Recarga acreditada con éxito.").build();
    }

    @GET
    @Path("/balance")
    @Consumes(MediaType.APPLICATION_JSON)
    public Optional<Double> showBalance(@QueryParam("userId") Long id) throws NoCustomerException {
        //TODO controles
        return controller.showBalance(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/postPay/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response linkCreditCard(@PathParam("userId") Long id, CreditCardDTO creditCard) throws NoCustomerException {
        //TODO controles
        controller.linkCreditCard(id, creditCard);

        return  Response.status(Response.Status.CREATED).entity("Tarjeta " +
                "vinculada con exito.: "+ id).build();
    }

    //prepay
    //postPay


}
