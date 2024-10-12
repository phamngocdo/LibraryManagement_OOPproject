package app.database;

import app.base.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AuthorDAO {
    public static String MAIN_TABLE = "authors";
    public static final String DOCUMENT_AUTHOR_TABLE = "document_author";

    // Lấy ra các tác giả của tác phẩm khi biết docId.
    public static ArrayList<Author> getAllAuthorFromDocId(String docId) {
        ArrayList<Author> authors = new ArrayList<>();
        String query = String.format(
                "SELECT a.author_id, a.name FROM %s AS a " +
                        "JOIN %s AS da ON a.author_id = da.author_id " +
                        "WHERE da.document_id = '%s'",
                MAIN_TABLE,
                DOCUMENT_AUTHOR_TABLE,
                docId
        );

        ResultSet resultSet = DatabaseManagement.getResultSetFromQuery(query);

        try {
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

    // Kiểm tra xem tác giả có tồn tại không
    public static boolean checkAuthorExist(String name) {
        String query = String.format("SELECT * FROM %s WHERE name = '%s'", MAIN_TABLE, name);
        ResultSet resultSet = DatabaseManagement.getResultSetFromQuery(query);
        try {
            return resultSet.next(); // Nếu có kết quả thì tác giả tồn tại
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Thêm tác giả
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
