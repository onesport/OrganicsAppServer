package application.registration;

import application.registration.UserDetails;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbUtils {
    //returns 1 if success else returns 0
    public static Integer addNewRegistration(UserDetails userDetails, Connection connection){
        String query="INSERT INTO userdetails (name,email,phone_no) VALUES ('"+userDetails.getName()+"','"+userDetails.getEmailId()+"','"+userDetails.getPhoneNo()+"')" ;
        System.out.println("query "+query);
        try {
            Statement statement = connection.createStatement();
            long affectedrows= statement.executeLargeUpdate(query,Statement.RETURN_GENERATED_KEYS);
            if(affectedrows==1L){
                ResultSet generatedKeys = statement.getGeneratedKeys();
                generatedKeys.next();
                return generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    //returns true if userid is free and available else returns false
    public static boolean checkuserIdaAvailability(String email,Connection connection){
        String query="SELECT email FROM userdetails WHERE email = '"+email+"'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()){
                return false;
            }else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String createUserTableQuery(){
        return "CREATE TABLE userdetails(userid BIGINT NOT NULL AUTO_INCREMENT,name VARCHAR(50) NOT NULL,email VARCHAR(50) NOT NULL,phone_no VARCHAR(10) NOT NULL,PRIMARY KEY (userid),UNIQUE (email),UNIQUE (phone_no));";
    }

//    INSERT INTO products (productname, productstatus,productdescription) VALUES ('oil','OUTOFSTOCK','hai');
    public static String createProductsTableQuery(){
        return "create table products(" +
                "   productid INT NOT NULL AUTO_INCREMENT," +
                "   productname VARCHAR(50) NOT NULL," +
                "   productstatus enum('AVAILABLE','OUTOFSTOCK','WITHDRAWN') default 'AVAILABLE'," +
                "   productdescription VARCHAR(1000) NOT NULL," +
                "   PRIMARY KEY (productid)" +
                ");";
    }
}
