package app.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class IdGenerator {
    private static final int ID_LENGTH = 12;

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
                PreparedStatement preparedStatement;
                preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(query.toString());
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
