package app.dao;

import java.sql.*;

public class DatabaseManagement {
    private static DatabaseManagement instance;
    private static Connection connection;
    private static final String DB_PATH = "src/main/resources/database/library.db";

    private DatabaseManagement() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DatabaseManagement getInstance() {
        if (instance == null) {
            instance = new DatabaseManagement();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}