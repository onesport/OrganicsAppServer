package com.onesport.organicappserver.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.onesport.organicappserver.entity.UserDetailsEntity;
import com.onesport.organicappserver.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

@RestController
public class UserRegirstrationController {

    @Autowired
    private UserDetailsService _userDetailsService;

    @RequestMapping(value = "/ping-user", method = RequestMethod.GET)
    public ResponseEntity<String> ping(){
        return new ResponseEntity<String>("pong-user".toString(), HttpStatus.OK);
    }

    //sample json
    //{"name":"jenison","email_id":"jenisongracious@gmail.com","phone_no":"9585508668"}
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> registerUser(@RequestBody byte[] data){
        InputStream is = new ByteArrayInputStream(data);
        try(Reader targetReader = new InputStreamReader(is)) {
            Gson gson = new GsonBuilder().create();
            UserDetailsEntity userDetails =gson.fromJson(targetReader,UserDetailsEntity.class);
            UserDetailsEntity result = null;

            if(checkAvailability(userDetails.getEmail())){
                result = _userDetailsService.saveUserDetails(userDetails);
            }else {
                JsonObject jsonObject=new JsonObject();
                jsonObject.addProperty("error","user ID is already registered");
                return new ResponseEntity<String>(jsonObject.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if(result!=null) {
                JsonObject jsonObject=new JsonObject();
                jsonObject.addProperty("success",result.getUserId());
                return new ResponseEntity<String>(jsonObject.toString(), HttpStatus.OK);
            }else {
                JsonObject jsonObject=new JsonObject();
                jsonObject.addProperty("error","unable to do registration");
                return new ResponseEntity<String>(jsonObject.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @RequestMapping(value = "/checkuseravailability", method = RequestMethod.GET)
    public ResponseEntity<String> checkIfUserIdIsAvailable(@RequestParam(value = "email")String email){

        if(checkAvailability(email)){
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("success","id_not_already_taken");
            return new ResponseEntity<String>(jsonObject.toString(),HttpStatus.OK);
        }else {
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("success","id_already_taken");
            return new ResponseEntity<String>(jsonObject.toString(),HttpStatus.OK);
        }
    }

    private boolean checkAvailability(String email){
       return _userDetailsService.FindByEmail(email);
    }
}
