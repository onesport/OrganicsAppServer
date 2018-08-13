package application;

import application.registration.UserDetails;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbUtils {
    //returns 1 if success else returns 0
    public static long addNewRegistration(UserDetails userDetails, Connection connection){
        String query="INSERT INTO userdetails (userid,name,email,phone_no) VALUES ('"+ userDetails.getUserId()+"','"+userDetails.getName()+"','"+userDetails.getEmailId()+"','"+userDetails.getPhoneNo()+"')" ;
        System.out.println("query "+query);
        try {
            Statement statement = connection.createStatement();
            return statement.executeLargeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    //returns true if userid is free and available else returns false
    public static boolean checkuserIdaAvailability(String userId,Connection connection){
        String query="SELECT userid FROM userdetails WHERE userid = '"+userId+"'";
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
        return "create table userdetails(" +
                "   userid VARCHAR(20) NOT NULL," +
                "   name VARCHAR(50) NOT NULL," +
                "   email VARCHAR(50) NOT NULL," +
                "   phone_no VARCHAR(10) NOT NULL," +
                "   PRIMARY KEY (userid)" +
                ");";
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
