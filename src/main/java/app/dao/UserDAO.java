package app.dao;

import app.base.Admin;
import app.base.Member;
import app.base.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAO {

    public static User getUserFromLogin(String username, String password) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM users ");
        query.append("WHERE username=? AND password=?");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                boolean isAdmin = resultSet.getString("role").equals("admin");
                if (isAdmin) {
                    return new Admin(resultSet);
                }

                return new Member(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static Member getMemberFromId(String id) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM users ");
        query.append("WHERE user_id=? AND role='member'");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Member(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static ArrayList<Member> getAllMember() {
        ArrayList<Member> members = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM users WHERE role = 'member'");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(query.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                members.add(
                        new Member(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return members;
    }

    public static void addMember(Member member) {
        if (member.getId() == null || member.getId().isEmpty()){
            member.setId(IdGenerator.createRandomIdInTable("users", "user_id"));
        }
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO users ");
        query.append("(user_id, username, password, first_name, last_name, birthday, email, phone_number, role) ");
        query.append("VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");

        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(query.toString());
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

    public static void removeMember(String memberId) {
        StringBuilder query = new StringBuilder();
        query.append("DELETE FROM users ");
        query.append("WHERE user_id=?");
        try {
            RatingDAO.removeRatingFromMemberId(memberId);
            ReceiptDAO.removeReceiptFromMemberId(memberId);
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, memberId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateMember(Member member) {
        StringBuilder query = new StringBuilder();
        query.append("UPDATE users ");
        query.append("SET password = ?, first_name = ?, last_name = ?, birthday = ?, email = ?, phone_number = ?");
        query.append("WHERE user_id = ?");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, member.getPassword());
            preparedStatement.setString(2, member.getFirstName());
            preparedStatement.setString(3, member.getLastName());
            preparedStatement.setString(4, member.getBirthday());
            preparedStatement.setString(5, member.getEmail());
            preparedStatement.setString(6, member.getPhoneNumber());
            preparedStatement.setString(7, member.getId());
            preparedStatement.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean checkUsernameExist(String username) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM users ");
        query.append("WHERE username=?");
        try{
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
