package app.database;

import app.base.Author;

import java.util.ArrayList;
import java.sql.*;

public class AuthorDAO {
    public static String MAIN_TABLE = "authors";

    public static ArrayList<Author> getAllAuthorFromDocId(String docId) {
        //

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
        //
    }
}
