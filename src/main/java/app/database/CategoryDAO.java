package app.database;

import app.base.Category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoryDAO {
    public static final String MAIN_TABLE = "categories";
    public static final String DOCUMENT_CATEGORY_TABLE = "document_category";

    // Lấy ra các thể loại khi biết docId.
    public static ArrayList<Category> getAllCategoryFromDocId(String docId) {
        ArrayList<Category> categories = new ArrayList<>();
        String query = String.format(
                "SELECT c.category_id, c.category FROM %s AS c " +
                        "JOIN %s AS dc " + "ON c.category_id = dc.category_id " +
                        "WHERE dc.document_id = '%s'",
                MAIN_TABLE, DOCUMENT_CATEGORY_TABLE, docId
        );

        ResultSet resultSet = DatabaseManagement.getResultSetFromQuery(query);

        try {
            while (resultSet.next()) {
                categories.add(new Category(
                        resultSet.getString("category_id"),
                        resultSet.getString("category")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;

    }

    // Thêm thể loại mới.
    public static void addCategory(Category category) {
        category.setId(DatabaseManagement.createRandomIdInTable(MAIN_TABLE, "category_id"));
        String query = String.format(
                "INSERT INTO %s (category_id, category) VALUES ('%s', '%s')",
                MAIN_TABLE,
                category.getId(),
                category.getCategory()
        );
        DatabaseManagement.executeUpdate(query);
    }
}