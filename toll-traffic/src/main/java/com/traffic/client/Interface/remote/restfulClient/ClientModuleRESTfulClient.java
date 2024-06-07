package com.traffic.client.Interface.remote.restfulClient;
import com.traffic.client.Interface.local.ClientController;
import com.traffic.client.domain.User.ForeignUser;
import com.traffic.client.domain.User.NationalUser;
import com.traffic.client.domain.User.User;
import com.traffic.dtos.account.CreditCardDTO;
import com.traffic.dtos.user.ForeignUserDTO;
import com.traffic.dtos.user.NationalUserDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.dtos.vehicle.TollPassDTO;
import com.traffic.dtos.vehicle.VehicleDTO;
import com.traffic.exceptions.IllegalRangeException;
import com.traffic.exceptions.NoCustomerException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ApplicationScoped
@Path("/TollCustomer")
@Transactional
public class ClientModuleRESTfulClient {

    @Inject
    private ClientController controller;

    //Usuarios
    //funciona
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/addUser")
    public UserDTO addUser(UserDTO user) {

        try{
            controller.addTollCostumer(user);

        }catch(Exception e){

            System.err.println(e.getMessage());
        }

        return user;
    }

    //curl -v http://localhost:8080/TollPass/api/TollCustomer/users

    //curl --cacert certificadoPrueba.pem --user test:1234 -v https://localhost:8443/TollPass/api/TollCustomer/administrator/users

    //@Path("/administrator/users")
    //funciona
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/users")
    public List<UserDTO> getAllUsers(){

        Optional<List<User>> users = controller.listUsers();

        List<UserDTO> usersDTO = new ArrayList<>();
        UserDTO userDTO = null;

        if(users.isPresent()){
            for (User user : users.get()){
                if(user instanceof ForeignUser){
                    userDTO = new ForeignUserDTO(user.getId(), user.getEmail(), user.getPassword(),
                            user.getName(), user.getCi(), null,
                            null, null);
                }else if (user instanceof NationalUser){
                    userDTO = new NationalUserDTO(user.getId(), user.getEmail(), user.getPassword(),
                            user.getName(), user.getCi(), null,
                            null, null, null);
                }
                usersDTO.add(userDTO);
            }

            return usersDTO;
        }

        return null;

    }

    //vehiculos

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/addVehicle/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addVehicle(@PathParam("userId") Long id, VehicleDTO vehicle){
        try{
            controller.linkVehicle(id, vehicle);
            return  Response.status(Response.Status.CREATED).entity("El vehiculo fue añadido con exito" +
                    " al usuario con  id: " + id).build();
        }catch (Exception e){
            return Response.status(Response.Status.NOT_FOUND).entity("Ocurrio un error: " +
                    e.getMessage()).build();
        }
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/removeVehicle/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeVehicle(@PathParam("userId") Long id, @QueryParam("vehicleId") Long vehicleId) {

        try{
            controller.unLinkVehicle(id, vehicleId);
            return  Response.status(Response.Status.CREATED).entity("El vehiculo fue eliminado con exito " +
                    "del usuario con id: " + id).build();
        }catch (Exception e){
            return Response.status(Response.Status.NOT_FOUND).entity("Ocurrio un error: " +
                    e.getMessage()).build();
        }

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

    @GET
    @Path("/showPassVehicle/{tagId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showPassVehicle(@QueryParam("from") String from,
                                                @QueryParam("to") String to, @PathParam("tagId") Long  tagId){

        try {
            LocalDate fromDate = LocalDate.parse(from, DateTimeFormatter.ISO_DATE);
            LocalDate toDate = LocalDate.parse(to, DateTimeFormatter.ISO_DATE);

            Optional<List<TollPassDTO>> listPass = controller.showPastPassagesVehicle(tagId, fromDate, toDate);

            if(listPass.isPresent()){
                System.out.println("antes de explotar");
                return Response.ok(listPass.get()).build();

            }else{

                return Response.status(Response.Status.NOT_FOUND).entity("No se encontraron pasadas" +
                        " con el vehiculo: " + tagId).build();

            }
        } catch (DateTimeParseException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Formato de fecha inválido. Utilice el formato ISO: yyyy-MM-dd.")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Algo salio mal " + e.getMessage())
                    .build();
        }

    }

//usr 1
    @PUT
    @Path("/addBalance")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response loadBalance(@QueryParam("balance") Double balance,
                                @QueryParam("userId") Long id) throws Exception {
        try{
            controller.loadBalance(id, balance);
            return  Response.status(Response.Status.CREATED).entity("Recarga acreditada con éxito.").build();
        }catch(Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Ocurrio un error: " +
            e.getMessage()).build();
        }

    }

    //funciona
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

    //funciona sin fecha.
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/linkCard/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response linkCreditCard(@PathParam("userId") Long id,
                                   CreditCardDTO creditCard){

        try{
            controller.linkCreditCard(id, creditCard);
            return  Response.status(Response.Status.CREATED).entity("Tarjeta " +
                    "vinculada con exito.: "+ id).build();
        }catch (Exception e){
            System.err.println(e.getMessage());

            return Response.status(Response.Status.NOT_FOUND).entity("Ocurrio un error: " +
                    e.getMessage()).build();
        }
    }


}


