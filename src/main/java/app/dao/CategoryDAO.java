package app.dao;

import app.base.Category;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoryDAO {

    public static ArrayList<Category> getAllCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM categories");

        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(query.toString());
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

    public static ArrayList<Category> getAllCategoryFromDocId(String docId) {
        ArrayList<Category> categories = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT c.category_id, c.category FROM categories AS c ");
        query.append("JOIN document_category AS dc ON c.category_id = dc.category_id ");
        query.append("WHERE dc.document_id = ?");

        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(query.toString());
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

    public static void addCategory(Category category) {
        if (category.getId() == null || category.getId().isEmpty()) {
            category.setId(IdGenerator.createRandomIdInTable("categories", "category_id"));
        }
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO categories ");
        query.append("(category_id, category) ");
        query.append("VALUES (?, ?)");
       try {
           PreparedStatement preparedStatement;
           preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(query.toString());
           preparedStatement.setString(1, category.getId());
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
            preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, category);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getOrAddCategory(Category category) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT category_id FROM categories WHERE category = ?");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, category.getCategory());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("category_id");
            } else {
                CategoryDAO.addCategory(category);
                return category.getId();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}