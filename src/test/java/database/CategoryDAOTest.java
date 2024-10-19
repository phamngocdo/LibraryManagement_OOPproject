package database;

import app.base.Category;
import app.database.CategoryDAO;
import app.database.DatabaseManagement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryDAOTest {

    @BeforeEach
    void setUp() {
        DatabaseManagement.setConnection();
    }

    @Test
    void testGetAllCategoryFromDocId() {
       String docId = "M7VT6I3XMC4C";
        ArrayList<Category> categories = CategoryDAO.getAllCategoryFromDocId(docId);

        // Kiểm tra danh sách thể loại không rỗng
        assertFalse(categories.isEmpty());

        boolean hasScienceCategory = categories.stream()
                .anyMatch(category -> category.getCategory().equals("Medical"));
        assertTrue(hasScienceCategory);
    }

    @Test
    void testCheckCategoryExist() {
        boolean categoryExists = CategoryDAO.checkCategoryExist("Medical");
        assertTrue(categoryExists);

        // Kiểm tra thể loại không tồn tại (ví dụ: "UnknownCategory")
        boolean unknownCategoryExists = CategoryDAO.checkCategoryExist("UnknownCategory");
        assertFalse(unknownCategoryExists);
    }

    @Test
    void testAddCategory() {
        // Thêm thể loại mới vào cơ sở dữ liệu
        Category newCategory = new Category("", "New Category");
        CategoryDAO.addCategory(newCategory);

        // Kiểm tra thể loại đã được thêm thành công
        boolean categoryExists = CategoryDAO.checkCategoryExist("New Category");
        assertTrue(categoryExists);
    }
}
