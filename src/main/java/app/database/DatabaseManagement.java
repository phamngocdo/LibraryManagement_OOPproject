package app.database;

import java.sql.*;
import java.util.Random;

public class DatabaseManagement {
    private static Connection connection;

    private static final String DB_PATH = "src/main/resources/library.db";
    private static final int ID_LENGTH = 12;

    public static void setConnection() {
        try {
            connection = DriverManager.getConnection("codec::sqlite:" + DB_PATH);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static ResultSet getResultSetFromQuery(String query){
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultSet;
    }

    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String createRandomIdInTable(String table, String idColumn) {
        Random random = new Random();
        String character = "1234567890QWERTYUIOPASDFGHJKLZXCVBNM";
        StringBuilder result;

        while (true) {
            result = new StringBuilder(ID_LENGTH);
            for (int i = 0; i < ID_LENGTH; i++) {
                int randomIndex = random.nextInt(character.length());
                result.append(character.charAt(randomIndex));
            }
            String query = String.format("SELECT * FROM %S WHERE %s = %s",
                                        table,
                                        idColumn,
                                        result);
            if (getResultSetFromQuery(query) == null) {
                return result.toString();
            }
        }
    }
}