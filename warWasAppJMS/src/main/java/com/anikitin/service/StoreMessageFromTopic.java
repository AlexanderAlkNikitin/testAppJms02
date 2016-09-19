package com.anikitin.service;

import generated.OrderActivatedCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by anikitin on 13.09.2016.
 */
@Component
public class StoreMessageFromTopic {

    private static final Logger LOG = LoggerFactory.getLogger(StoreMessageFromTopic.class);

    private Integer countAddedObject=0;
    private Set<OrderActivatedCard> orderActivatedCardArrayList = new HashSet<>();


    public void add(OrderActivatedCard orderActivatedCard) {
        countAddedObject++;
        orderActivatedCardArrayList.add(orderActivatedCard);
        LOG.info("Added object to store " + orderActivatedCard);
    }

    public int getSize() {
        return orderActivatedCardArrayList.size();
    }

    public OrderActivatedCard get() {
        return orderActivatedCardArrayList.size() == 0 ? null : orderActivatedCardArrayList.iterator().next();
    }

    public void clear() {
        orderActivatedCardArrayList.clear();
        countAddedObject = 0;
        LOG.info("Clear store");
    }

    public Integer getCountAddedObject() {
        return countAddedObject;
    }
}
