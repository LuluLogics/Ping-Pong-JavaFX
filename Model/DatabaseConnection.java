package com.example.pongstage1.Model;
import com.example.pongstage1.View.PingPongGame;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private final String url = "jdbc:mysql://127.0.0.1:3306/pingponggame";
    private final String user = "root";
    private final String password = "52Thewillows";

    private DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        } else {
            try {
                if (instance.connection == null || instance.connection.isClosed()) {
                    instance = new DatabaseConnection();
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error connecting to the database", e);
            }
        }
        return instance;
    }
    public Connection getConnection() {
        return connection;
    }
}
