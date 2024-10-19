package database;

import app.base.Author;
import app.database.AuthorDAO;
import app.database.DatabaseManagement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AuthorDAOTest {

    private Author testAuthor;

    @BeforeEach
    void setUp() {
        DatabaseManagement.setConnection();
    }

    @Test
    void testAddAuthor() {
        Author newAuthor = new Author(null, "New Author");
        AuthorDAO.addAuthor(newAuthor);

        // Kiểm tra xem tác giả mới đã được thêm chưa
        assertTrue(AuthorDAO.checkAuthorExist("New Author"));
    }

    @Test
    void testCheckAuthorExist() {
        testAuthor = new Author(null, "Test Author");
        AuthorDAO.addAuthor(testAuthor);
        // Kiểm tra xem tác giả đã tồn tại
        assertTrue(AuthorDAO.checkAuthorExist(testAuthor.getName()));
        // Kiểm tra xem tác giả không tồn tại
        assertFalse(AuthorDAO.checkAuthorExist("Non-Existent Author"));
    }

    @Test
    void testGetAllAuthorFromDocId() {
        String docId = "M7VT6I3XMC4C";

        ArrayList<Author> authors = AuthorDAO.getAllAuthorFromDocId(docId);

        assertNotNull(authors); // Kiểm tra danh sách không null
        assertFalse(authors.isEmpty()); // Kiểm tra danh sách không rỗng

        // Kiểm tra xem có ít nhất một tác giả nào đó
        Author firstAuthor = authors.get(0);
        assertNotNull(firstAuthor.getId());
        assertNotNull(firstAuthor.getName());

        // Kiểm tra xem tác giả có trong danh sách hay không
        boolean authortest = authors.stream()
                .anyMatch(author -> author.getName().equals("Jules J. Berman"));
        assertTrue(authortest);
    }
}
