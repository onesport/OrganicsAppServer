package com.onesport.organicappserver.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.onesport.organicappserver.entity.ProductsEntity;
import com.onesport.organicappserver.service.ProductsService;
import com.onesport.organicappserver.utils.Fileutils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class ProductController {
    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private Environment env;

    @Autowired
    private ProductsService _productsService;

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public ResponseEntity<String> ping(){
        System.out.println("jenison"+env.getProperty("product_images_location"));
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
            return new ResponseEntity<String>(jsonObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(name.length()>50){
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("error","product name cannot be more than 50 characters");
            return new ResponseEntity<String>(jsonObject.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(!(status.equals(ProductsEntity.ProductAvailability.AVAILABLE.name()) ||
                status.equals(ProductsEntity.ProductAvailability.OUTOFSTOCK.name()) ||
                status.equals(ProductsEntity.ProductAvailability.WITHDRAWN.name()))){
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("error","product status is not acceptable");
            return new ResponseEntity<String>(jsonObject.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            if(description.length()>=1000){
                JsonObject jsonObject=new JsonObject();
                jsonObject.addProperty("error","product description cannot be more than 1000 characters");
                return new ResponseEntity<String>(jsonObject.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
            }
            new JsonParser().parse(description).getAsJsonObject();
        }catch (JsonSyntaxException e){
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("error","product description does not contain all necessities");
            return new ResponseEntity<String>(jsonObject.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            var operationType=ProductsEntity.ProductAvailability.AVAILABLE;
            if(status.equals(ProductsEntity.ProductAvailability.OUTOFSTOCK.name())){
                operationType=ProductsEntity.ProductAvailability.OUTOFSTOCK;
            }else if(status.equals(ProductsEntity.ProductAvailability.WITHDRAWN.name())){
                operationType=ProductsEntity.ProductAvailability.WITHDRAWN;
            }

            ProductsEntity _product = new ProductsEntity();
            _product.setProductdescription(description);
            _product.setProductname(name);
            _product.setProductstatus(operationType);
            Integer integer = _productsService.saveProducts(_product);
            if(integer!=null){
                Fileutils.writeImageToTemp(file,String.valueOf(integer),env.getProperty("product_images_location"));
                JsonObject jsonObject=new JsonObject();
                jsonObject.addProperty("success",integer);
                return new ResponseEntity<String>(jsonObject.toString(),HttpStatus.OK);
            }else {
                JsonObject jsonObject=new JsonObject();
                jsonObject.addProperty("error","an internal server error has occured");
                return new ResponseEntity<String>(jsonObject.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e) {
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("error","the image is not a valid file");
            return new ResponseEntity<String>(jsonObject.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/getproductimage" ,method = RequestMethod.GET )
    public ResponseEntity getProductimage(@RequestParam(value = "productid")String id){
        byte[] product_images_locations = Fileutils.getFile(id, env.getProperty("product_images_location"));
        if(product_images_locations==null){
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("error","an internal server error has occured");
            return new ResponseEntity<String>(jsonObject.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
        }else {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<byte[]>(product_images_locations,headers,HttpStatus.OK);
        }

    }

    @RequestMapping(value = "/getallProducts", method = RequestMethod.GET)
    public ResponseEntity<String> getallProducts(@RequestParam(value = "status")String status){
        JsonArray jsonArray=new JsonArray();
        List<ProductsEntity> data = _productsService.getAllProducts(ProductsEntity.ProductAvailability.AVAILABLE);
            for(int i=0;i<data.size();i++){
                JsonObject jsonObject=new JsonObject();
                ProductsEntity productsEntity = data.get(i);
                jsonObject.addProperty("id",productsEntity.getProductId());
                jsonObject.addProperty("name",productsEntity.getProductname());
                jsonObject.addProperty("description",productsEntity.getProductdescription());
                jsonArray.add(jsonObject);
            }
        if(data.size()<=0){
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("error","no products available right now");
            return new ResponseEntity<String>(jsonObject.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
        }else {
            JsonObject finalObject=new JsonObject();
            finalObject.add("data",jsonArray);
            return new ResponseEntity<String>(finalObject.toString(),HttpStatus.OK);
        }
    }
}
