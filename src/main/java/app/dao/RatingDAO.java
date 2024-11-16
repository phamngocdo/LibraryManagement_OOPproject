package app.dao;

import app.base.Rating;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RatingDAO {

    public static Rating getRatingFromId(String ratingId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ratings ");
        query.append("WHERE rating_id = ?");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, ratingId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Rating(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static ArrayList<Rating> getAllRatingFromDocId(String docId) {
        //kiểm tra document_id
        ArrayList<Rating> ratings = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ratings ");
        query.append("WHERE document_id = ?");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, docId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ratings.add(new Rating(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ratings;
    }

    public static ArrayList<Rating> getAllRating() {
        //trả về table ratings
        ArrayList<Rating> ratings = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ratings");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getConnection().prepareStatement(query.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ratings.add(new Rating(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ratings;
    }


    public static void addRating(Rating rating) {
        if (rating.getId().isEmpty()) {
            rating.setId(DatabaseManagement.createRandomIdInTable("ratings", "rating_id"));
        }
        //thêm thuộc tính vào db
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ratings ");
        query.append("(rating_id, user_id, document_id, rating_score, comment) ");
        query.append("VALUES(?, ?, ?, ?, ?)");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, rating.getId());
            preparedStatement.setString(2, rating.getMemberId());
            preparedStatement.setString(3, rating.getDocId());
            preparedStatement.setInt(4, rating.getRatingScore());
            preparedStatement.setString(5, rating.getComment());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeRating(String ratingId) {
        //xóa rating theo id
        StringBuilder query = new StringBuilder();
        query.append("DELETE FROM ratings ");
        query.append("WHERE rating_id = ?");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, ratingId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeRatingFromMemberId(String userId) {
        //xóa rating theo id
        StringBuilder query = new StringBuilder();
        query.append("DELETE FROM ratings ");
        query.append("WHERE user_id = ?");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
