package application;

import application.fileutils.Fileutils;
import application.registration.UserDetails;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    //{"name":"jenison","email_id":"jenisongracious@gmail.com","phone_no":"9585508668","userid":"jenisonleo"}
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> registerUser(@RequestBody byte[] data){
        InputStream is = new ByteArrayInputStream(data);
        try(Reader targetReader = new InputStreamReader(is)) {
            Gson gson = new GsonBuilder().create();
            UserDetails userDetails =gson.fromJson(targetReader,UserDetails.class);
            ////TODO check if user id is already taken
            long result = DbUtils.addNewRegistration(userDetails, connection);
            if(result==1L) {
                return ResponseEntity.status(HttpStatus.OK).build();
            }else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @RequestMapping(value = "/checkuseravailability", method = RequestMethod.GET)
    public ResponseEntity<String> checkIfUserIdIsAvailable(@RequestParam(value = "userid") String userId){

        if(DbUtils.checkuserIdaAvailability(userId,connection)){
            return new ResponseEntity<String>("id_not_already_taken",HttpStatus.OK);
        }else {
            return new ResponseEntity<String>("id_already_taken",HttpStatus.OK);
        }
    }






}
