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
        assertTrue(AuthorDAO.checkAuthorExist("New Author"));
    }

    @Test
    void testCheckAuthorExist() {
        Author testAuthor = new Author("", "Test Author");
        AuthorDAO.addAuthor(testAuthor);
        assertTrue(AuthorDAO.checkAuthorExist(testAuthor.getName()));
        assertFalse(AuthorDAO.checkAuthorExist("Non-Existent Author"));
    }

    @Test
    void testGetAllAuthorFromDocId() {
        String docId = "m7vT6i3xmC4C";

        ArrayList<Author> authors = AuthorDAO.getAllAuthorFromDocId(docId);

        assertNotNull(authors);
        assertFalse(authors.isEmpty());

        Author firstAuthor = authors.getFirst();
        assertNotNull(firstAuthor.getId());
        assertNotNull(firstAuthor.getName());

        boolean authortest = authors.stream()
                .anyMatch(author -> author.getName().equals("Jules J. Berman"));
        assertTrue(authortest);
    }
}
