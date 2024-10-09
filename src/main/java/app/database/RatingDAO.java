package app.database;

import app.base.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RatingDAO {
    public static final String MAIN_TABLE = "ratings";

    public static Rating getRatingFromId(String ratingId) {
        //kiểm tra rating_id
        String query = String.format("SELECT * FROM %s WHERE rating_id = '%s'", MAIN_TABLE, ratingId);
        ResultSet resultSet = DatabaseManagement.getResultSetFromQuery(query);
        try {
            if (resultSet.next()) {
                // Chuyển đổi ResultSet thành đối tượng Rating
                return new Rating(
                        resultSet.getString("rating_id"),
                        resultSet.getString("user_id"),
                        resultSet.getString("document_id"),
                        resultSet.getInt("rating_score"),
                        resultSet.getString("comment")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static ArrayList<Rating> getAllRatingFromDocId(String docId) {
        //kiểm tra document_id
        ArrayList<Rating> ratings = new ArrayList<>();
        String query = String.format("SELECT * FROM %s WHERE document_id = '%s'", MAIN_TABLE, docId);
        ResultSet resultSet = DatabaseManagement.getResultSetFromQuery(query);
        try {
            while (resultSet.next()) {
                ratings.add(new Rating(
                        resultSet.getString("rating_id"),
                        resultSet.getString("user_id"),
                        resultSet.getString("document_id"),
                        resultSet.getInt("rating_score"),
                        resultSet.getString("comment")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ratings;
    }

    public static ArrayList<Rating> getAllRating() {
        //trả về table ratings
        ArrayList<Rating> ratings = new ArrayList<>();
        String query = String.format("SELECT * FROM %s", MAIN_TABLE);
        ResultSet resultSet = DatabaseManagement.getResultSetFromQuery(query);
        try {
            while (resultSet.next()) {
                ratings.add(new Rating(
                        resultSet.getString("rating_id"),
                        resultSet.getString("user_id"),
                        resultSet.getString("document_id"),
                        resultSet.getInt("rating_score"),
                        resultSet.getString("comment")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ratings;
    }

    public static void addRating(Rating rating) {
        rating.setId(DatabaseManagement.createRandomIdInTable(MAIN_TABLE, "rating_id"));
        //thêm thuộc tính vào db
        String query = String.format("INSERT INTO %s (rating_id, user_id, document_id, " +
                        "rating_score, comment) " +
                        "VALUES ('%s', '%s', '%s', %d, '%s')",
                MAIN_TABLE,
                rating.getId(),
                rating.getUserId(),
                rating.getDocId(),
                rating.getRatingScore(),
                rating.getComment()
        );
        DatabaseManagement.executeUpdate(query);
    }

    public static void removeRating(String ratingId) {
        //xóa rating theo id
        String query = String.format("DELETE FROM %s WHERE rating_id = '%s'", MAIN_TABLE, ratingId);
        DatabaseManagement.executeUpdate(query);
    }
}
