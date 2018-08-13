package application.orders;

import com.google.gson.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@RestController
public class OrderController {

    private Connection connection=null;

    public OrderController(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/mysql", "root", "*****");
            if (connection == null) {
                System.out.println("connection3 null");
            } else {
                System.out.println("connection3 not null");
            }
            JsonObject jsonObject=new JsonObject();
            JsonArray array=new JsonArray();
            for (int i=0;i<5;i++){
                JsonObject j=new JsonObject();
                j.addProperty(String.valueOf(i),String.valueOf(i));
                array.add(j);
            }
            jsonObject.add("orders",array);
            System.out.println(jsonObject.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/placeorder", method = RequestMethod.POST)
    public ResponseEntity<String> placeOrder(@RequestParam(value = "orderdetails") String details, @RequestParam(value = "userid")String userid) {
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
            if(asJsonArray.size()>=0) {
                throw new JsonParseException("not more than 10 orders can be placed");
            }
            ////todo verify that the order keys are present live and the count is valid
//            OrderDbUtils.addnewOrder(connection,)

        }catch (JsonParseException e){

        }
        return null;
    }
}
