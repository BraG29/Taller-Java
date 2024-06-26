package com.traffic.toll.infraestructure.messaging;

import com.traffic.toll.domain.entities.CommonTariff;
import com.traffic.toll.domain.entities.PreferentialTariff;
import com.traffic.toll.domain.entities.Tag;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;

@ApplicationScoped
public class SendMessageQueueUtil {

    @Inject
    private JMSContext jmsContext;
    @Resource( lookup = "java:jboss/exported/jms/queue/nationalPayment")
    private Queue queue;


    public void sendMessage(Tag tag,
                            CommonTariff commonTariff,
                            PreferentialTariff preferentialTariff){

        PaymentMessage paymentMessage = new PaymentMessage(
                commonTariff.getAmount(),
                preferentialTariff.getAmount(),
                tag.getId()
        );
        jmsContext.createProducer().send(queue, paymentMessage.toJson());
    }

}
