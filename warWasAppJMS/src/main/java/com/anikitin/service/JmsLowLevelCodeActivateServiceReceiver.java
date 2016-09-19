package com.anikitin.service;

import com.ibm.mq.jms.MQTopicConnectionFactory;
import com.ibm.msg.client.jms.JmsTopicConnectionFactory;
import generated.OrderActivatedCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.connection.DelegatingConnectionFactory;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MarshallingMessageConverter;
import org.springframework.stereotype.Service;

import javax.jms.*;
//import com.ibm.mq.jms.*;


/**
 * Created by baldessarinii on 09.09.16.
 */
@Service
public class JmsLowLevelCodeActivateServiceReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(JmsLowLevelCodeActivateServiceReceiver.class);
    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private JmsTemplate jmsTopicTemplate;

    @Autowired
    private Topic topDurable;
    @Autowired
    private MarshallingMessageConverter marshaller;
    @Autowired
    private StoreMessageFromTopic storeMessageFromTopic;
    private TopicSession session;
    private TopicConnection connection;
    private TopicSubscriber topicSubscriber;

    public void openConsumerDurableSubscribe() {
        try {
            connection=((DelegatingConnectionFactory)connectionFactory).createTopicConnection();
            connection.setClientID("durTopic");
            connection.start();
            session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            topicSubscriber = session.createDurableSubscriber(topDurable, "subName");
            topicSubscriber.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    OrderActivatedCard orderActivatedCard = null;
                    try {
                        orderActivatedCard = (OrderActivatedCard) marshaller.fromMessage(message);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                    LOG.info("Received " + orderActivatedCard);
                    storeMessageFromTopic.add(orderActivatedCard);
                }
            });
        } catch (JMSException e) {
            LOG.info(e.getMessage());
        }
    }

    public void stopConsumer() throws JMSException {
        if (connection != null && topicSubscriber != null && session != null) {
            topicSubscriber.close();
            session.close();
            connection.close();
        }
    }
}
