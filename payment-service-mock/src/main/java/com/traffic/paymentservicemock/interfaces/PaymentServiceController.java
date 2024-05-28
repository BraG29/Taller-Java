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
public class PaymentServiceController {

    // http://localhost:8080/payment-service/api/paymentCheck/
    @POST
    @Path("/paymentCheck")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkCreditCard(CreditCardDTO creditCard){
        Boolean output = true;

        //TODO: logica aleatoria para los pagos
        return Response.ok(output).build();
    }

}
