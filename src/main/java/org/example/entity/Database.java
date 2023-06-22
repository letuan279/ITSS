package org.example.entity;

import org.example.utils.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Connection connect = null;
    public static Connection getConnection() {
        if(connect != null) return  connect;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(Config.DB_URL, Config.DB_USERNAME, Config.DB_PASSWORD);
            System.out.println("Connection is Successful to the database");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connect;
    }

    public static void main(String[] args) {
        Database.getConnection();
    }
}
