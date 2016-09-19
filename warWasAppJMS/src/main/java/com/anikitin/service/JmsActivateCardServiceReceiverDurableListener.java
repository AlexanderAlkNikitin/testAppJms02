package com.anikitin.service;

import generated.OrderActivatedCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.support.converter.MarshallingMessageConverter;
import org.springframework.stereotype.Service;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * Created by anikitin on 16.09.2016.
 */
@Service
//@MessageDriven(activationConfig = {
//    @ActivationConfigProperty(propertyName = "destinationType",propertyValue = "javax.jms.Topic"),
//        @ActivationConfigProperty(propertyValue = "destinationLookup",propertyName = "jms/durableTOPIC "),
//        @ActivationConfigProperty(propertyName =  "connectionFactoryLookup",propertyValue =  "jms/topicQueue")
//},mappedName = "jms/Spec")
public class JmsActivateCardServiceReceiverDurableListener implements MessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(JmsActivateCardServiceReceiverDurableListener.class);
    @Autowired
    private MarshallingMessageConverter oxmMessageConverter;
    @Autowired
    private StoreMessageFromTopic storeMessageFromTopic;

    @Override
    public void onMessage(Message message) {
        OrderActivatedCard orderActivatedCard = null;
        try {
            orderActivatedCard = (OrderActivatedCard) oxmMessageConverter.fromMessage(message);
        } catch (JMSException e) {
            LOG.info(e.getMessage());
        }
        LOG.info("received: " + orderActivatedCard);
        storeMessageFromTopic.add(orderActivatedCard);
    }
}
