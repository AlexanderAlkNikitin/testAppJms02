package com.anikitin.service;

import generated.OrderActivatedCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.*;

/**
 * Created by anikitin on 07.09.2016.
 */
@Service
public class JmsActivateCardServiceSender {
    @Autowired
    private JmsTemplate jmsQueueTemplate;
    @Autowired
    private JmsTemplate jmsTopicTemplate;
    //    @Autowired
//    private JmsTemplate jmsTopicTemplate;
    @Autowired
    private Queue des;

    private static final Logger LOG = LoggerFactory.getLogger(JmsActivateCardServiceSender.class);


    public void sendObjectXmlToQueue(OrderActivatedCard orderActivatedCard) {
        LOG.info("sending test object");
        this.jmsQueueTemplate.convertAndSend("jms/INPUT", orderActivatedCard);
//        jmsTemplate.browse(new BrowserCallback<Message>() {
//            @Override
//            public Message doInJms(Session session, QueueBrowser browser) throws JMSException {
//                session.commit();
//                return session.createMapMessage();
//            }
//        });
        LOG.info("test object sended");
    }


    @Transactional
    public void sendObjectXmlTo(String destination, OrderActivatedCard orderActivatedCard) {
        LOG.info("Send message to " + destination.toString());
        this.jmsTopicTemplate.convertAndSend(destination, orderActivatedCard);
    }
    @Transactional
    public void sendObjectXmlToQueue(String destination, OrderActivatedCard orderActivatedCard) {
        LOG.info("Send message to " + destination.toString());
        this.jmsQueueTemplate.convertAndSend(destination, orderActivatedCard);
    }


    @Transactional
    public void sendAndReceive(final String destination) {
        LOG.debug(destination.toString());
        this.jmsQueueTemplate.sendAndReceive(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                LOG.debug("create message consumer");
                Queue queue = session.createQueue("INPUT");
                MessageConsumer messageConsumer = session.createConsumer(queue);
                return messageConsumer.receive(1000);
            }
        });
    }

}
