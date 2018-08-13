package application.products;

import application.DbUtils;
import application.fileutils.Fileutils;
import com.google.gson.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
public class ProductController {
    private Connection connection = null;
    private enum productAvailability{
        AVAILABLE,OUTOFSTOCK,WITHDRAWN
    }
    public ProductController() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/mysql", "root", "*****");
            if (connection == null) {
                System.out.println("connection2 null");
            } else {
                System.out.println("connection2 not null");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @RequestMapping(value = "/uploadproduct", method = RequestMethod.POST)
    public ResponseEntity<String> uploadproductimage(@RequestParam(value = "image") MultipartFile file, @RequestParam(value = "productname")String name, @RequestParam(value = "status")String status,@RequestParam(value = "description") String description){
        if(file.getSize()>(1024L*100L)){
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("error","image size is greater than 100kb");
            return new ResponseEntity<String>(jsonObject.toString(),HttpStatus.OK);
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
            Integer integer = ProductDbUtils.addNewProduct(name, operationType, description, connection);
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

    @RequestMapping(value = "/getallProducts", method = RequestMethod.GET)
    public ResponseEntity<String> getallProducts(@RequestParam(value = "status")String status){
        try {
            JsonArray jsonArray=new JsonArray();
            ResultSet resultSet = ProductDbUtils.getallProducts(connection);
            while(resultSet.next()){
                JsonObject jsonObject=new JsonObject();
                jsonObject.addProperty("id",resultSet.getString("productid"));
                jsonObject.addProperty("name",resultSet.getString("productname"));
                jsonObject.addProperty("description",resultSet.getString("productdescription"));
                jsonArray.add(jsonObject);
            }
            JsonObject finalObject=new JsonObject();
            finalObject.add("data",jsonArray);
            return new ResponseEntity<String>(finalObject.toString(),HttpStatus.OK);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("error","unable to fetch products");
        return new ResponseEntity<String>(jsonObject.toString(),HttpStatus.OK);
    }

}
