package app.database;

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
            throw new RuntimeException("Failed to establish a database connection.", e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    //Hàm này dùng để truy vấn có kết quả trả về
    public static ResultSet getResultSetFromQuery(String query) {
        ResultSet resultSet;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Error executing query: " + query, e);
        }
        return resultSet;
    }

    //Hàm này được sử dụng cho câu lệnh xóa, thêm, cập nhật
    public static void executeUpdate(String query) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error executing update: " + query, e);
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error closing connection.", e);
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

            String query = String.format("SELECT * FROM %s WHERE %s = ?", table, idColumn);
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, result.toString());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (!resultSet.next()) {
                        return result.toString();
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error generating random ID.", e);
            }
        }
    }
}
