package app.database;

import app.base.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AuthorDAO {
    public static String MAIN_TABLE = "authors";
    public static final String DOCUMENT_AUTHOR_TABLE = "document_author";

    public static ArrayList<Author> getAllAuthorFromDocId(String docId) {
        //
        ArrayList<Author> authors = new ArrayList<>();
        String query = String.format(
                "SELECT a.author_id, a.name FROM %s AS a JOIN %s AS da " +
                        "ON a.author_id = da.author_id WHERE da.document_id = '%s'",
                MAIN_TABLE,
                DOCUMENT_AUTHOR_TABLE,
                docId
        );

        ResultSet resultSet = DatabaseManagement.getResultSetFromQuery(query);
        try {
            while (resultSet.next()) {
                // Chuyển đổi ResultSet thành đối tượng Author
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

    public static void addAuthor(Author author) {
        author.setId(DatabaseManagement.createRandomIdInTable(MAIN_TABLE, "author_id"));
        String query = String.format(
                "INSERT INTO %s (author_id, name) VALUES ('%s', '%s')",
                MAIN_TABLE,
                author.getId(),
                author.getName()
        );
        DatabaseManagement.executeUpdate(query);
    }
}
