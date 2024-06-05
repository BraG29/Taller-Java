package com.traffic.suciveservicemock;

import com.traffic.suciveservicemock.interfaces.LicensePlateDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Random;

@ApplicationScoped
@Path("/controller")
public class SuciveServiceController {

    @POST
    @Path("/checkSucive")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkSucive(LicensePlateDTO licensePlate) {

        if(licensePlate.getLicensePlateNumber().equals("ABC123")) {
            return Response.ok("SUCCESS (TEST LicenseNumber)").build();
        }
        if(licensePlate.getLicensePlateNumber().equals("SAN666")||licensePlate.getLicensePlateNumber().equals("san666")) {
            return Response.status(400, "ERROR (TEST LicenseNumber)").build();
        }
        Random r = new Random();
        int rand = r.nextInt(10);

        if(rand % 2 == 0) {
            return Response.status(400, "ERROR (DEFAULT RESPONSE)").build();
        }
        return Response.ok("SUCCESS (DEFAULT RESPONSE)").build();
    }
}
