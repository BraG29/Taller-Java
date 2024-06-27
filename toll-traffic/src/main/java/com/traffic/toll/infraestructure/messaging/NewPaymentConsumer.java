package com.traffic.toll.infraestructure.messaging;

import com.traffic.dtos.vehicle.IdentifierDTO;
import com.traffic.dtos.vehicle.TagDTO;
import com.traffic.toll.application.PaymentService;
import com.traffic.toll.application.VehicleService;
import com.traffic.toll.domain.entities.CommonTariff;
import com.traffic.toll.domain.entities.NationalVehicle;
import com.traffic.toll.domain.entities.PreferentialTariff;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.inject.Inject;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;

import java.util.NoSuchElementException;

@MessageDriven(
        activationConfig = {
                @ActivationConfigProperty(
                        propertyName = "destinationType",
                        propertyValue = "jakarta.jms.Queue"),
                @ActivationConfigProperty(
                        propertyName = "destinationLookup",
                        propertyValue = "java:app/jms/NationalPaymentQueue"),
                @ActivationConfigProperty
                        //Default: 15
                        (propertyName = "maxSession", propertyValue = "1")
        }
)
public class NewPaymentConsumer implements MessageListener {
    @Inject
    private VehicleService vehicleService;
    @Inject
    private PaymentService paymentService;
    @Inject
    private SendMessageQueueUtil messageQueueUtil;


    @Override
    public void onMessage(Message message) {
        try {
            String body = message.getBody(String.class);
            PaymentMessage paymentMessage = PaymentMessage.readFromJson(body);
            IdentifierDTO identifier = new TagDTO(paymentMessage.tagId(), "");
            //Si el vehiculo llega a la queue es porque es vehiculo nacional seguro.
            NationalVehicle vehicle = (NationalVehicle) vehicleService.getByIdentifier(identifier).orElseThrow();

            PreferentialTariff preferentialTariff = new PreferentialTariff(
                    0L, //No nos interesa el ID solamente el costo, ya que sabemos que la tarifa existe
                    paymentMessage.preferentialTariff());
            CommonTariff commonTariff = new CommonTariff(
                    0L,
                    paymentMessage.commonTariff());

            if(paymentService.tryPayment(vehicle, preferentialTariff) || paymentService.trySucive(vehicle, commonTariff)){
                System.out.println(
                        "Se le ha cobrado al Usuario con Vehiculo Nacional de matricula: " + vehicle.getLicensePlate().getLicensePlateNumber());

            }else{
                messageQueueUtil.sendMessage(vehicle.getTag(), commonTariff, preferentialTariff);
            }

        } catch (JMSException e) {
            System.err.printf("Error (de tipo %s): %s\n", e.getClass(), e.getMessage());

        } catch (NoSuchElementException e){
            System.err.printf("Error (en clase %s): No se encontro el Vehiculo", this.getClass());
        }
    }
}
