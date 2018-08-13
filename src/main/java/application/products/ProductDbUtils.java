package application.products;

import application.registration.UserDetails;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductDbUtils {

    public static Integer addNewProduct(String name,String status,String description, Connection connection){
        String query="INSERT INTO products (productname,productstatus,productdescription) VALUES ('"+ name+"','"+status+"','"+description+"')" ;
        System.out.println("query "+query);
        Statement statement=null;
        try {
            statement = connection.createStatement();
            long affectedrows= statement.executeLargeUpdate(query,Statement.RETURN_GENERATED_KEYS);
            if(affectedrows==1L){
                ResultSet generatedKeys = statement.getGeneratedKeys();
                generatedKeys.next();
                return generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            ////TODO:do the rollback here
            e.printStackTrace();
        }

        return null;
    }

    public static ResultSet getallProducts(Connection connection) throws SQLException {
        String query="SELECT productid,productname,productdescription FROM products WHERE productstatus = 'AVAILABLE' OR productstatus = 'OUTOFSTOCK'";
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

}
