package com.anikitin.service;

import generated.OrderActivatedCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by anikitin on 07.09.2016.
 */
@Service
public class JmsActivateCardServiceReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(JmsActivateCardServiceReceiver.class);

    @Autowired
    private JmsTemplate jmsQueueTemplate;
    @Autowired
    private JmsActivateCardServiceSender jmsActivateCardServiceSender;
    @Autowired
    private JmsTransactionManager transactionManager;
//    @Autowired
//    private Destination optionalDestination;


    public OrderActivatedCard receiveOrderActivateCard(String destination) {
        OrderActivatedCard orderActivatedCard=(OrderActivatedCard) this.jmsQueueTemplate.receiveAndConvert(destination);
        LOG.info("received: " + orderActivatedCard);
        return orderActivatedCard;
    }

    public OrderActivatedCard receiveOrderActivateCardFromOrder() {
        return (OrderActivatedCard) jmsQueueTemplate.receiveAndConvert("jms/INPUT");
    }

    @Transactional(rollbackFor = Exception.class)
    public OrderActivatedCard sendAfterReceiveRollBack() throws Exception {
        LOG.info("receiving test Object");
        OrderActivatedCard receivedOrder = receiveOrderActivateCard("jms/INPUT");
        LOG.info("test object received");
        LOG.info("sending test object after receive");
        jmsActivateCardServiceSender.sendObjectXmlToQueue("jms/optionalQueue", receivedOrder);
        if (1 == 1) throw new Exception();
        LOG.info("test object sended after receive");
        return receivedOrder;

    }


//    public OrderActivatedCard receiveOrderActivateCardFromOrder() {
//        return (OrderActivatedCard) jmsQueueTemplate.receiveAndConvert(optionalDestination);
//    }
//
//    public OrderActivatedCard receiveOrderActivateCard(Destination destination) {
//
//        return (OrderActivatedCard) this.jmsQueueTemplate.receiveAndConvert(destination);
//    }
}
