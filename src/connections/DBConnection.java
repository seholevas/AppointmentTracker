package connections;


import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Steven
 */
public class DBConnection 
{
    // variables
    private static final String databaseName = "U06CXN";
    private static final String DB_URL = "jdbc:mysql://52.206.157.109/" + databaseName;
    private static final String username = "U06CXN";
    private static final String password = "53688725028";
    private static final String driver = "com.mysql.jdbc.Driver";
    private static Connection connection;

    //makes the connection to the database    
    public static void makeConnection() throws ClassNotFoundException, SQLException, Exception
    {
        Class.forName(driver);
        connection = (Connection) DriverManager.getConnection(DB_URL,username,password);
        System.out.println("Connection Successful!");
        
    }
    
    //closes the connection the the database
    public static void closeConnection() throws ClassNotFoundException, SQLException, Exception
    {
        connection.close();
        System.out.println("Connection Closed!");   
    }
    
    //returns the connection to the database
    public static Connection getConnection()
    {
        return connection;
    }
}
