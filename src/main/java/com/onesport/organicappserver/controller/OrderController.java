package com.onesport.organicappserver.controller;

import com.google.gson.*;
import com.onesport.organicappserver.entity.OrderEntity;
import com.onesport.organicappserver.entity.UserDetailsEntity;
import com.onesport.organicappserver.service.OrderService;
import com.onesport.organicappserver.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserDetailsService userDetailsService;


    @RequestMapping(value = "/placeorder", method = RequestMethod.POST)
    public ResponseEntity<String> placeOrder(@RequestParam(value = "orderitems") String details, @RequestParam(value = "userid")Integer userid) {
        try {
            JsonParser parser = new JsonParser();
            JsonObject parse = parser.parse(details).getAsJsonObject();
            JsonElement element = parse.get("orders");
            if(element==null){
                throw new JsonParseException("orders not found");
            }
            JsonArray asJsonArray = element.getAsJsonArray();
            if(asJsonArray.size()==0){
                throw new JsonParseException("orders not found");
            }
            if(asJsonArray.size()>=10) {
                throw new JsonParseException("not more than 10 orders can be placed");
            }
            ////todo verify that the order keys are present live and the count is valid
            Optional<UserDetailsEntity> userDetailsEntity = userDetailsService.findbyUserId(userid);
            if(userDetailsEntity.isPresent()) {
                OrderEntity orderEntity=new OrderEntity();
                orderEntity.setOrderitems(details);
                orderEntity.setOrderStatustatus(OrderEntity.OrderStatus.ORDERPLACED);
                orderEntity.setUserId(userDetailsEntity.get());
                JsonObject local = new JsonObject();
                local.addProperty("time", System.currentTimeMillis());
                local.addProperty("message", "Your order has been succesfully placed");
                JsonArray array = new JsonArray();
                array.add(local);
                orderEntity.setOrderDetails(array.toString());
                OrderEntity resultEntity = orderService.addNewOrder(orderEntity);
                JsonObject jsonObject=new JsonObject();
                jsonObject.addProperty("success",resultEntity.getOrederId());
                return new ResponseEntity<String>(jsonObject.toString(),HttpStatus.OK);
            }else {
                JsonObject jsonObject=new JsonObject();
                jsonObject.addProperty("error","user id not found");
                return new ResponseEntity<String>(jsonObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch (JsonParseException e){

        }
        return null;
    }
}


//    create table orders(orderid BIGINT NOT NULL AUTO_INCREMENT,orderitems VARCHAR(200) NOT NULL,userid BIGINT NOT NULL,orderstatus enum('ORDERPLACED','ORDERDELIVERED','ORDERCANCELED'),orderdetails VARCHAR(1000) NOT NULL, PRIMARY KEY (orderid),FOREIGN KEY (userid) REFERENCES userdetails(userid))