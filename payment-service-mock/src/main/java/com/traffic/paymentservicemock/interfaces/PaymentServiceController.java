package com.traffic.paymentservicemock.interfaces;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
        Boolean output = true;

        if(creditCard.getCardNumber().equals("6666-6666-6666-6666")){
            output = false;
            return Response.ok(output).build();
        }

        if(creditCard.getCardNumber().equals("1111-1111-1111-1111")){
            output = true;
            return Response.ok(output).build();
        }

        int r = (int) Math.random();
        if(r == 5){
            output = false;
            return Response.ok(output).build();
        }

        return Response.ok(output).build();
    }

}
