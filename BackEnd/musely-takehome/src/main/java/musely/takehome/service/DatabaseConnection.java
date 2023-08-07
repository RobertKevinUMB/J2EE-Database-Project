package musely.takehome.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
    	// Register the MySQL JDBC driver
    	Class.forName("com.mysql.cj.jdbc.Driver");
    	// Configure the database connection parameters
        String url = "jdbc:mysql://musely-db.cqidggopfbf9.us-east-2.rds.amazonaws.com:3306/db1";
        String username = "admin";
        String password = "9003093225";
       
        // Create and return the database connection
        return DriverManager.getConnection(url,username,password);
    }

    
}
