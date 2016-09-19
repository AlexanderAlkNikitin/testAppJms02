package com.anikitin.web;

import com.anikitin.service.JmsActivateCardServiceReceiver;
import com.anikitin.service.JmsActivateCardServiceSender;
import com.anikitin.service.JmsLowLevelCodeActivateServiceReceiver;
import com.anikitin.service.StoreMessageFromTopic;
import generated.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jms.Destination;
import javax.jms.JMSException;

/**
 * Created by anikitin on 15.09.2016.
 */
@Controller
@RequestMapping("/scenarios")
public class FirstScenarioController {
    private static final Logger LOG = LoggerFactory.getLogger(FirstScenarioController.class);
    @Autowired
    private JmsActivateCardServiceSender jmsActivateCardServiceSender;
    @Autowired
    private JmsActivateCardServiceReceiver jmsActivateCardServiceReceiver;
    @Autowired
    private JmsLowLevelCodeActivateServiceReceiver jmsLowLevelCodeActivateServiceReceiver;


    @Autowired
    private Destination optionalDestination;
    @Autowired
    private StoreMessageFromTopic storeMessageFromTopic;
    @Autowired
    private DefaultMessageListenerContainer jmsContainer2;
    @Autowired
    private DefaultMessageListenerContainer jmsContainer3;
    @Autowired
    private DefaultMessageListenerContainer jmsContainer;

    @RequestMapping(value = "/firstScenario")
    public String firstScenario() throws InterruptedException {
        LOG.info("Starting test send to queue and receive");
        storeMessageFromTopic.clear();
        OrderActivatedCard toQueue = getOrderActivatedCard();
        jmsActivateCardServiceSender.sendObjectXmlToQueue(getOrderActivatedCard());
        OrderActivatedCard fromQueue = jmsActivateCardServiceReceiver.receiveOrderActivateCard("jms/INPUT");
        String result = toQueue.equals(fromQueue) ? "Objects equals" : "not equals";
        LOG.info(result);
        LOG.info("Starting test send to topic and receive");
        storeMessageFromTopic.clear();
        OrderActivatedCard toTopic = getOrderActivatedCard();
        jmsActivateCardServiceSender.sendObjectXmlTo("jms/TOPICIN", toTopic);
        Thread.sleep(1000);
        LOG.info("Added objects = " + storeMessageFromTopic.getCountAddedObject());
        result = toQueue.equals(storeMessageFromTopic.get()) ? "Objects equals" : "not equals";
        LOG.info(result);
        return "index";
    }

    @RequestMapping(value = "/secondScenario")
    public String secondScenario() {
        LOG.info("Starting test send to topic and drop");
        storeMessageFromTopic.clear();
        LOG.info("Stopping listeners");
        jmsContainer.shutdown();
        jmsContainer2.shutdown();
        OrderActivatedCard orderActivatedCard = getOrderActivatedCard();
        jmsActivateCardServiceSender.sendObjectXmlTo("jms/TOPICIN", orderActivatedCard);
        LOG.info("Start listeners");
        jmsContainer.start();
        jmsContainer2.start();
        LOG.info("Objects in store = " + storeMessageFromTopic.getSize());
        LOG.info("Added in store = " + storeMessageFromTopic.getCountAddedObject());
        return "index";
    }

    @RequestMapping(value = "/thirdScenario")
    public String thirdScenario() throws InterruptedException, JMSException {
        storeMessageFromTopic.clear();
//        jmsContainer3.initialize();
        OrderActivatedCard orderActivatedCard = getOrderActivatedCard();
//        jmsLowLevelCodeActivateServiceReceiver.stopConsumer();
//        jmsLowLevelCodeActivateServiceReceiver.openConsumerDurableSubscribe();
        jmsActivateCardServiceSender.sendObjectXmlTo("jms/durableTOPIC", orderActivatedCard);
        jmsActivateCardServiceSender.sendObjectXmlTo("jms/durableTOPIC", orderActivatedCard);
        jmsActivateCardServiceSender.sendObjectXmlTo("jms/durableTOPIC", orderActivatedCard);
        Thread.sleep(1000);
//        jmsLowLevelCodeActivateServiceReceiver.stopConsumer();
        jmsContainer3.shutdown();
        Thread.sleep(1000);
        LOG.info("Off listener");
        jmsActivateCardServiceSender.sendObjectXmlTo("jms/durableTOPIC", orderActivatedCard);
        jmsActivateCardServiceSender.sendObjectXmlTo("jms/durableTOPIC", orderActivatedCard);
        jmsActivateCardServiceSender.sendObjectXmlTo("jms/durableTOPIC", orderActivatedCard);
        LOG.info("on listener");
//        jmsLowLevelCodeActivateServiceReceiver.openConsumerDurableSubscribe();
        jmsContainer3.start();
        jmsContainer3.initialize();
        Thread.sleep(5000);
        LOG.info("Objects in store = " + storeMessageFromTopic.getSize());
        LOG.info("Added in store = " + storeMessageFromTopic.getCountAddedObject());
//        assertTrue(storeMessageFromTopic.getSize() == 1);
//        assertEquals(orderActivatedCard, storeMessageFromTopic.get());
        return "index";
    }

    @RequestMapping(value = "/fourthScenario")
    public String fourthScenario(){
        LOG.info("Start test send after receive");
        OrderActivatedCard orderActivatedCard = getOrderActivatedCard();
        jmsActivateCardServiceSender.sendObjectXmlToQueue(orderActivatedCard);
        jmsActivateCardServiceSender.sendAndReceive("jms/optionalQueue");
        OrderActivatedCard orderActivatedCard1= jmsActivateCardServiceReceiver.receiveOrderActivateCard("jms/optionalQueue");
        LOG.info("equals? "+orderActivatedCard.equals(orderActivatedCard1));
//        assertEquals(orderActivatedCard,jmsActivateCardServiceReceiver.receiveOrderActivateCard(optionalDestination));

        return "index";
    }


    @RequestMapping(value = "/fifthScenario")
    @Transactional(rollbackFor = Exception.class)
    public String fifthScenario() throws InterruptedException {

        LOG.info("Start test rollBack");
        OrderActivatedCard orderActivatedCard = getOrderActivatedCard();
        jmsActivateCardServiceSender.sendObjectXmlToQueue(orderActivatedCard);
        Thread.sleep(1000);
        try {
            OrderActivatedCard forwardingOrder = jmsActivateCardServiceReceiver.sendAfterReceiveRollBack();
        } catch (Exception e) {
            e.printStackTrace();
            OrderActivatedCard forwardedOrder = jmsActivateCardServiceReceiver.receiveOrderActivateCardFromOrder();
        }


        return "index";
    }


    private OrderActivatedCard getOrderActivatedCard() {
        Person person = new Person();
        person.setID(1);
        person.setFirstName("Ivan");
        person.setLastName("Ivanov");
        Card card = new Card();
        card.setID(1);
        card.setPerson(person);
        card.setLimit(10000);
        card.setStatus(StatusType.DISABLED);

        OrderActivatedCard orderActivatedCard = new OrderActivatedCard();
        orderActivatedCard.setID(1);
        orderActivatedCard.setCard(card);
        orderActivatedCard.setSolve(SolveType.YES);
        return orderActivatedCard;
    }
}
