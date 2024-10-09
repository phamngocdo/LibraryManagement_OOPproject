package app.database;

import app.base.Category;

import java.util.ArrayList;
import java.sql.*;

public class CategoryDAO {
    public static final String MAIN_TABLE = "categories";

    public static ArrayList<Category> getAllCategoryFromDocId(String docId) {
        return new ArrayList<>();

    }

    public static void addCategory(Category category) {
        category.setId(DatabaseManagement.createRandomIdInTable(MAIN_TABLE,
                "category_id"));
        //
    }
}
