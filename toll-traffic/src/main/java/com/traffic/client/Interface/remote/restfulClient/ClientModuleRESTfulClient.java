package com.traffic.client.Interface.remote.restfulClient;
import com.traffic.client.Interface.local.ClientController;
import com.traffic.client.domain.User.User;
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
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@ApplicationScoped
@Path("/TollCustomer")
@Transactional
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

    //curl -v http://localhost:8080/TollPass/api/TollCustomer/users

    //curl --cacert certificadoPrueba.pem --user test:1234 -v https://localhost:8443/TollPass/api/TollCustomer/administrator/users

    //@Path("/administrator/users")

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/users")
    public List<User> getAllUsers(){

        Optional<List<User>> users = controller.listUsers();

        return users.orElse(null);

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
    public Response getVehicles(@PathParam("userId") Long id) throws NoCustomerException {

        Optional<List<VehicleDTO>> vehicles = controller.showLinkedVehicles(id);

        if(vehicles.isPresent()){
            return Response.ok(vehicles.get()).build();
        }else{
            return Response.status(Response.Status.NOT_FOUND).entity("No se encontraron vehiculos del usuario"
                    + " con el id: " + id ).build();
        }
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/showPass/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showPass(@PathParam("userId") Long id, @QueryParam("from") String from,
                                                @QueryParam("to") String to)
            throws NoCustomerException, IllegalRangeException {

        LocalDate fromDate = LocalDate.parse(from, DateTimeFormatter.ISO_DATE);
        LocalDate toDate = LocalDate.parse(to, DateTimeFormatter.ISO_DATE);
        Optional<List<TollPassDTO>> listPass = controller.showPastPassages(id, fromDate, toDate);

        if(listPass.isPresent()){
            return Response.ok(listPass.get()).build();
        }else{
            return Response.status(Response.Status.NOT_FOUND).entity("No se encontraron pasadas del usuario" +
                    " con el id: " + id ).build();
        }

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/showPassVehicle")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showPassVehicle(@QueryParam("from") String from,
                                                @QueryParam("to") String to, TagDTO tag)

            throws NoCustomerException, IllegalRangeException {

        LocalDate fromDate = LocalDate.parse(from, DateTimeFormatter.ISO_DATE);
        LocalDate toDate = LocalDate.parse(to, DateTimeFormatter.ISO_DATE);

        Optional<List<TollPassDTO>> listPass = controller.showPastPassagesVehicle(tag, fromDate, toDate);

        if(listPass.isPresent()){

            return Response.ok(listPass.get()).build();

        }else{

            return Response.status(Response.Status.NOT_FOUND).entity("No se encontraron pasadas" +
                    " con el vehiculo: " + tag).build();

        }

    }

    //cuentas
/*
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/Accounts/{tag}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccountsByTag(@PathParam("tag") Long tag){

        TagDTO tagDTO = new TagDTO(tag);

        Optional<List<AccountDTO>> listAccount = controller.getAccountByTag(tagDTO);

        if(listAccount.isPresent()){
            return Response.ok(listAccount.get()).build();
        }else{
            return Response.status(Response.Status.NOT_FOUND).entity("No se encontraron cuentas del usuario" +
                    " con tag:  " + tag).build();
        }

    }
*/

    @PUT
    @Path("/addBalance")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response loadBalance(@QueryParam("balance") Double balance, @QueryParam("userId") Long id) throws NoCustomerException {
        //TODO controles
        controller.loadBalance(id, balance);
        return  Response.status(Response.Status.CREATED).entity("Recarga acreditada con éxito.").build();
    }

    @GET
    @Path("/balance/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response showBalance(@PathParam("userId") Long id) throws NoCustomerException {
        Optional<Double> balance = controller.showBalance(id);
        if (balance.isPresent()) {
            return Response.ok(balance.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("No se encontró balance para el usuario" +
                    " con id: " + id).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/linkCard/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response linkCreditCard(@PathParam("userId") Long id,
                                   CreditCardDTO creditCard) throws NoCustomerException {

        //TODO controles
        System.out.println("Nombre tarjeta: " + creditCard.getName());

        controller.linkCreditCard(id, creditCard);
        return  Response.status(Response.Status.CREATED).entity("Tarjeta " +
                "vinculada con exito.: "+ id).build();

    }


}


