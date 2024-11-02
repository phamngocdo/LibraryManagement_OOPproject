package base;

import app.base.Document;
import app.database.DatabaseManagement;
import app.database.DocumentDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//All test are correct
public class DocumentTest {

    @BeforeEach
    void setUp() {
        DatabaseManagement.setConnection();
    }

    @Test
    void testGetAuthorsToString() {
        Document testDocument = DocumentDAO.getDocFromId("THWi6ag6DgQC");
        assertNotNull(testDocument);
        String authorsString = testDocument.getAuthorsToString();
        assertEquals("Michael Roberts, Neil Ingram", authorsString);
    }

    @Test
    void testGetCategoriesToString() {
        Document testDocument = DocumentDAO.getDocFromId("GjGZEVddXSYC");
        assertNotNull(testDocument);
        String categoriesString = testDocument.getCategoriesToString();
        assertEquals("Business, Economics", categoriesString);
    }
}
