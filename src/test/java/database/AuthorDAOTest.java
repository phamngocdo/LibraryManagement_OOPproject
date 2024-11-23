package database;

import app.base.Author;
import app.dao.AuthorDAO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

//All test are correct
public class AuthorDAOTest {

    @Test
    void testAddAuthor() {
        Author newAuthor = new Author("", "New Author");
        AuthorDAO.addAuthor(newAuthor);

        // Kiểm tra xem tác giả mới đã được thêm chưa
        assertTrue(AuthorDAO.checkAuthorExist("New Author"));
    }

    @Test
    void testCheckAuthorExist() {
        Author testAuthor = new Author("", "Test Author");
        AuthorDAO.addAuthor(testAuthor);
        // Kiểm tra xem tác giả đã tồn tại
        assertTrue(AuthorDAO.checkAuthorExist(testAuthor.getName()));
        // Kiểm tra xem tác giả không tồn tại
        assertFalse(AuthorDAO.checkAuthorExist("Non-Existent Author"));
    }

    @Test
    void testGetAllAuthorFromDocId() {
        String docId = "m7vT6i3xmC4C";

        ArrayList<Author> authors = AuthorDAO.getAllAuthorFromDocId(docId);

        assertNotNull(authors); // Kiểm tra danh sách không null
        assertFalse(authors.isEmpty()); // Kiểm tra danh sách không rỗng

        // Kiểm tra xem có ít nhất một tác giả nào đó
        Author firstAuthor = authors.getFirst();
        assertNotNull(firstAuthor.getId());
        assertNotNull(firstAuthor.getName());

        // Kiểm tra xem tác giả có trong danh sách hay không
        boolean authortest = authors.stream()
                .anyMatch(author -> author.getName().equals("Jules J. Berman"));
        assertTrue(authortest);
    }
}
