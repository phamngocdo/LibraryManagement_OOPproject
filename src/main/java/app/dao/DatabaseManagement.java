package app.dao;

import java.sql.*;
import java.util.Random;

public class DatabaseManagement {
    private static Connection connection;

    private static final String DB_PATH = "src/main/resources/database/library.db";
    private static final int ID_LENGTH = 12;

    public static void setConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static String createRandomIdInTable(String table, String idColumn) {
        Random random = new Random();
        String character = "1234567890QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm";
        StringBuilder result;

        while (true) {
            result = new StringBuilder(ID_LENGTH);
            for (int i = 0; i < ID_LENGTH; i++) {
                int randomIndex = random.nextInt(character.length());
                result.append(character.charAt(randomIndex));
            }

            StringBuilder query = new StringBuilder();
            query.append("SELECT ").append(idColumn).append(" FROM ").append(table);
            query.append(" WHERE ").append(idColumn).append(" = ?");
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query.toString());
                preparedStatement.setString(1, result.toString());
                ResultSet resultSet = preparedStatement.executeQuery();
                if (!resultSet.next()) {
                    return result.toString();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}