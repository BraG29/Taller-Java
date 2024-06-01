package com.traffic.paymentservicemock.interfaces;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Random;

// http://localhost:8080/payment-service/api/
@ApplicationScoped
@Path("/controller")
public class PaymentServiceController {

    // http://localhost:8080/payment-service/api/controller/paymentCheck/
    @POST
    @Path("/paymentCheck")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkCreditCard(CreditCardDTO creditCard){

        if(creditCard.getCardNumber().equals("6666-6666-6666-6666")){
            return Response.status(400,"Default Error").build();
        }

        if(creditCard.getCardNumber().equals("1111-1111-1111-1111")){

            return Response.ok("Forced Success").build();
        }

        Random r = new Random();
        int rand = r.nextInt(5);

        if(rand == 4){
            return Response.status(400, "ERROR (1 of 5 failed)").build();
        }
        return Response.ok("SUCCESS (DEFAULT RESPONSE)").build();

    }

}
