package app.base;

import app.dao.DocumentDAO;
import app.dao.UserDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class User {
    protected String id;
    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String birthday;
    protected String email;
    protected String phoneNumber;

    public User(String id, String username, String password, String firstName,
                String lastName, String birthday, String email, String phoneNumber){
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public User(ResultSet resultSets) throws SQLException {
        this.id = resultSets.getString("user_id");
        this.username = resultSets.getString("username");
        this.password = resultSets.getString("password");
        this.firstName = resultSets.getString("first_name");
        this.lastName = resultSets.getString("last_name");
        this.birthday = resultSets.getString("birthday");
        this.email = resultSets.getString("email");
        this.phoneNumber = resultSets.getString("phone_number");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ArrayList<Document> seeTopRatingDoc(int number) {
        return DocumentDAO.getBestRatingDocuments(number);
    }

    public static User login( String username, String password) {
        return UserDAO.getUserFromLogin(username, password);
    }
}
