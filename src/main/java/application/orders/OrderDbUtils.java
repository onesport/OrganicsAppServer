package application.orders;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OrderDbUtils {

//    {"orders":[{"0":"0"},{"1":"1"},{"2":"2"},{"3":"3"},{"4":"4"}]}
public static Integer addnewOrder(Connection connection,String orders,String description,String userid){
String query="INSERT INTO orders (orderitems, orderdetails,userid) VALUES ('"+orders+"','"+description+"','"+userid+"')";
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



    public static String createOrderTableQuery(){
        return "create table orders(" +
                "   orderid BIGINT NOT NULL AUTO_INCREMENT," +
                "   orderitems VARCHAR(200) NOT NULL," +
                "   userid VARCHAR(20) NOT NULL,"+
                "   orderstatus enum('ORDERPLACED','ORDERDELIVERED','ORDERCANCELED') default 'ORDERPLACED'," +
                "   orderdetails VARCHAR(1000) NOT NULL," +
                "   PRIMARY KEY (orderid)," +
                "   FOREIGN KEY (userid) REFERENCES userdetails(userid)"+
                ");";
    }
}
