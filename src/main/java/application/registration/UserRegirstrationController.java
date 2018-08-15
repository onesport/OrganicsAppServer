package application.registration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@RestController
public class UserRegirstrationController {
    private Connection connection = null;
    
    public UserRegirstrationController(){
        System.out.println("init");
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/mysql", "root", "*****");
            if (connection == null) {
                System.out.println("connection null");
            } else {
                System.out.println("connection not null");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String sayHello(@RequestParam(value = "name") String name) {
        return "Hello " + name + "!";
    }

    //sample json
    //{"name":"jenison","email_id":"jenisongracious@gmail.com","phone_no":"9585508668"}
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> registerUser(@RequestBody byte[] data){
        InputStream is = new ByteArrayInputStream(data);
        try(Reader targetReader = new InputStreamReader(is)) {
            Gson gson = new GsonBuilder().create();
            UserDetails userDetails =gson.fromJson(targetReader,UserDetails.class);
            ////TODO check if user id is already taken
            Integer result = RegistrationDbUtils.addNewRegistration(userDetails, connection);
            if(result!=null) {
                JsonObject jsonObject=new JsonObject();
                jsonObject.addProperty("success",result);
                return new ResponseEntity<String>(jsonObject.toString(),HttpStatus.OK);
            }else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @RequestMapping(value = "/checkuseravailability", method = RequestMethod.GET)
    public ResponseEntity<String> checkIfUserIdIsAvailable(@RequestParam(value = "email") String email){

        if(RegistrationDbUtils.checkuserIdaAvailability(email,connection)){
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("success","id_not_already_taken");
            return new ResponseEntity<String>(jsonObject.toString(),HttpStatus.OK);
        }else {
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("success","id_already_taken");
            return new ResponseEntity<String>(jsonObject.toString(),HttpStatus.OK);
        }
    }






}
