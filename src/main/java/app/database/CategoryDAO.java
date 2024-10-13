package app.database;

import app.base.Category;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoryDAO {
    // Lấy ra các thể loại khi biết docId.
    public static ArrayList<Category> getAllCategoryFromDocId(String docId) {
        ArrayList<Category> categories = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT c.category_id, c.category FROM categories AS c ");
        query.append("JOIN document_category AS dc ON c.category_id = dc.category_id ");
        query.append("WHERE dc.document_id = ?");

        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, docId);
            ResultSet resultSet = preparedStatement.executeQuery();
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
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO categories ");
        query.append("(category_id, category) ");
        query.append("VALUES (?, ?)");
       try {
           PreparedStatement preparedStatement;
           preparedStatement = DatabaseManagement.getConnection().prepareStatement(query.toString());
           preparedStatement.setString(1, category.getCategory());
           preparedStatement.setString(2, category.getCategory());
           preparedStatement.executeUpdate();
       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
    }

    public static boolean checkCategoryExist(String category) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT category_id FROM categories ");
        query.append("WHERE category = ?");

        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, category);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // Nếu có kết quả thì thể loại tồn tại
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}