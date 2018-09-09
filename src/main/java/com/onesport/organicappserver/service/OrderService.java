package com.onesport.organicappserver.service;

import com.onesport.organicappserver.entity.OrderEntity;
import com.onesport.organicappserver.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public OrderEntity addNewOrder(OrderEntity orderEntity){
       return orderRepository.save(orderEntity);
    }

}
