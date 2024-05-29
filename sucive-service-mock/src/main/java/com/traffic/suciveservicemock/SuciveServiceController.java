package com.traffic.suciveservicemock;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Path("/controller")
public class SuciveServiceController {

    @GET
    @Path("/checkSucive")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkSucive() {
        return Response.ok("Hello World!").build();
    }
}
