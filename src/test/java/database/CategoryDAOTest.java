package database;

import app.base.Category;
import app.dao.CategoryDAO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

//All test are correct
public class CategoryDAOTest {
    @Test
    void testGetAllCategoryFromDocId() {
       String docId = "m7vT6i3xmC4C";
        ArrayList<Category> categories = CategoryDAO.getAllCategoryFromDocId(docId);
        assertFalse(categories.isEmpty());

        boolean hasScienceCategory = categories.stream()
                .anyMatch(category -> category.getCategory().equals("Medical"));
        assertTrue(hasScienceCategory);
    }

    @Test
    void testCheckCategoryExist() {
        boolean categoryExists = CategoryDAO.checkCategoryExist("Medical");
        assertTrue(categoryExists);

        boolean unknownCategoryExists = CategoryDAO.checkCategoryExist("UnknownCategory");
        assertFalse(unknownCategoryExists);
    }

    @Test
    void testAddCategory() {
        Category newCategory = new Category(null, "New Category");
        CategoryDAO.addCategory(newCategory);

        boolean categoryExists = CategoryDAO.checkCategoryExist("New Category");
        assertTrue(categoryExists);
    }
}
