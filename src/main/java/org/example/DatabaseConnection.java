
package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private String url = "jdbc:mysql://localhost:3306/hospitalsys";
    private String username = "root";
    private String password = "borybory#2005";
    private DatabaseConnection() throws SQLException{
        try {
            this.connection = DriverManager.getConnection(url, username, password);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static DatabaseConnection getInstance() throws SQLException {
        if(instance == null){
            instance = new DatabaseConnection();
        }
        return instance;
    }
    public Connection getConnection() {
        return connection;
    }
}