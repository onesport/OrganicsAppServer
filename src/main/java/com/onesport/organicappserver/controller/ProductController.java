package com.onesport.organicappserver.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.onesport.organicappserver.entity.ProductsEntity;
import com.onesport.organicappserver.service.ProductsService;
import com.onesport.organicappserver.utils.Fileutils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class ProductController {
    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);
    private enum productAvailability{
        AVAILABLE,OUTOFSTOCK,WITHDRAWN
    }

    @Autowired
    private ProductsService _productsService;

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public ResponseEntity<String> ping(){
        return new ResponseEntity<String>("pong".toString(), HttpStatus.OK);
    }

    @RequestMapping(value = "/uploadproduct", method = RequestMethod.POST)
    public ResponseEntity<String> uploadproductimage(@RequestParam(value = "image") MultipartFile file,
                                                     @RequestParam(value = "productname")String name,
                                                     @RequestParam(value = "status")String status,
                                                     @RequestParam(value = "description") String description){
        if(file.getSize()>(1024L*100L)){
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("error","image size is greater than 100kb");
            return new ResponseEntity<String>(jsonObject.toString(), HttpStatus.OK);
        }
        if(name.length()>50){
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("error","product name cannot be more than 50 characters");
            return new ResponseEntity<String>(jsonObject.toString(),HttpStatus.OK);
        }
        if(!(status.equals(productAvailability.AVAILABLE.name()) ||
                status.equals(productAvailability.OUTOFSTOCK.name()) ||
                status.equals(productAvailability.WITHDRAWN.name()))){
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("error","product status is not acceptable");
            return new ResponseEntity<String>(jsonObject.toString(),HttpStatus.OK);
        }
        try {
            if(description.length()>=1000){
                JsonObject jsonObject=new JsonObject();
                jsonObject.addProperty("error","product description cannot be more than 1000 characters");
                return new ResponseEntity<String>(jsonObject.toString(),HttpStatus.OK);
            }
            new JsonParser().parse(description).getAsJsonObject();
        }catch (JsonSyntaxException e){
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("error","product description does not contain all necessities");
            return new ResponseEntity<String>(jsonObject.toString(),HttpStatus.OK);
        }
        try {
            String operationType="AVAILABLE";//default
            if(status.equals(productAvailability.OUTOFSTOCK.name())){
                operationType="OUTOFSTOCK";
            }else if(status.equals(productAvailability.WITHDRAWN.name())){
                operationType="WITHDRAWN";
            }
        //    Integer integer = ProductDbUtils.addNewProduct(name, operationType, description, connection);

            ProductsEntity _product = new ProductsEntity();
            _product.setProductdescription(description);
            _product.setProductname(name);
            _product.setProductstatus(operationType);
            Integer integer = _productsService.SaveProducts(_product);
            if(integer!=null){
                Fileutils.writeImageToTemp(file,String.valueOf(integer));
            }else {
                JsonObject jsonObject=new JsonObject();
                jsonObject.addProperty("error","an internal server error has occured");
                return new ResponseEntity<String>(jsonObject.toString(),HttpStatus.OK);
            }
            return new ResponseEntity<String>("success",HttpStatus.OK);
        } catch (IOException e) {
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("error","the image is not a valid file");
            return new ResponseEntity<String>(jsonObject.toString(),HttpStatus.OK);
        }
    }
}
