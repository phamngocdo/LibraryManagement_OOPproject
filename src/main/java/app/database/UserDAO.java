package app.database;

import app.base.Admin;
import app.base.Member;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAO {

    public static Admin getAdminFromSignIn(String username, String password) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM users ");
        query.append("WHERE username=? AND password=? AND role='admin'");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Admin(
                        resultSet.getString("user_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("birthday"),
                        resultSet.getString("email"),
                        resultSet.getString("phone_number")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // Get Member by ID
    public static Member getMemberFromId(String id) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM users ");
        query.append("WHERE user_id=? AND role='member'");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Member(
                        resultSet.getString("user_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("birthday"),
                        resultSet.getString("email"),
                        resultSet.getString("phone_number")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // Get Member by username and password
    public static Member getMemberFromSignIn(String username, String password) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM users ");
        query.append("WHERE username=? AND password=? AND role='member'");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Member(
                        resultSet.getString("user_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("birthday"),
                        resultSet.getString("email"),
                        resultSet.getString("phone_number")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // Retrieve all members
    public static ArrayList<Member> getAllMember() {
        ArrayList<Member> members = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM users WHERE role='member'");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getConnection().prepareStatement(query.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                members.add(
                        new Member(
                                resultSet.getString("user_id"),
                                resultSet.getString("username"),
                                resultSet.getString("password"),
                                resultSet.getString("first_name"),
                                resultSet.getString("last_name"),
                                resultSet.getString("birthday"),
                                resultSet.getString("email"),
                                resultSet.getString("phone_number")
                        ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return members;
    }

    // Add a new member
    public static void addMember(Member member) {
        if (member.getId().isEmpty()){
            member.setId(DatabaseManagement.createRandomIdInTable("users", "user_id"));
        }
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO users ");
        query.append("(user_id, username, password, first_name, last_name, birthday, email, phone_number, role) ");
        query.append("VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");

        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, member.getId());
            preparedStatement.setString(2, member.getUsername());
            preparedStatement.setString(3, member.getPassword());
            preparedStatement.setString(4, member.getFirstName());
            preparedStatement.setString(5, member.getLastName());
            preparedStatement.setString(6, member.getBirthday());
            preparedStatement.setString(7, member.getEmail());
            preparedStatement.setString(8, member.getPhoneNumber());
            preparedStatement.setString(9, "member");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Remove a member
    public static void removeMember(String memberId) {
        StringBuilder query = new StringBuilder();
        query.append("DELETE FROM users ");
        query.append("WHERE user_id=?");
        try {
            RatingDAO.removeRatingFromMemberId(memberId);
            ReceiptDAO.removeReceiptFromMemberId(memberId);
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, memberId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // Update a member
    public static void updateMember(Member member) {
        removeMember(member.getId());
        addMember(member);
    }

    // Check if username exists
    public static boolean checkUsernameExist(String username) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM users ");
        query.append("WHERE username=?");
        try{
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // Return true if username exists
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
