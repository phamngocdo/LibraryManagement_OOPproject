package app.dao;

import app.base.Author;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AuthorDAO {

    public static ArrayList<Author> getAllAuthors() {
        ArrayList<Author> authors = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM authors");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(query.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                authors.add(new Author(
                        resultSet.getString("author_id"),
                        resultSet.getString("name")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return authors;
    }

    public static ArrayList<Author> getAllAuthorFromDocId(String docId) {
        ArrayList<Author> authors = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT a.author_id, a.name FROM authors AS a ");
        query.append("JOIN document_author AS da ON a.author_id = da.author_id ");
        query.append("WHERE da.document_id = ?");

        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, docId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                authors.add(new Author(
                        resultSet.getString("author_id"),
                        resultSet.getString("name")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return authors;
    }

    public static boolean checkAuthorExist(String name) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT author_id FROM authors ");
        query.append("WHERE name = ?");

        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // Nếu có kết quả thì tác giả tồn tại
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addAuthor(Author author) {
        if (author.getId() == null || author.getId().isEmpty()) {
            author.setId(IdGenerator.createRandomIdInTable("authors", "author_id"));
        }
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO authors ");
        query.append("(author_id, name) ");
        query.append("VALUES (?, ?)");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, author.getId());
            preparedStatement.setString(2, author.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
