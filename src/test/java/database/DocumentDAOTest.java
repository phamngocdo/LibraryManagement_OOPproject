package database;

import app.base.Document;
import app.dao.DatabaseManagement;
import app.dao.DocumentDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
//All Test are correct
public class DocumentDAOTest {

    @BeforeEach
    void setUp() {
        DatabaseManagement.setConnection();
    }

    @Test
    void testAddDocument() {
        Document newDocument = new Document("", "New Document", "", 3 ,5,
                2, 5.0, 100, "description", "pulisher",
                "publishDate", "https://example.com/new_image.jpg", null,
                null, null);
        DocumentDAO.addDocument(newDocument);
        Document fetchedDocument = DocumentDAO.getDocFromId(newDocument.getId());
        assertNotNull(fetchedDocument);
        assertEquals("New Document", fetchedDocument.getTitle());
    }

    @Test
    void testGetDocFromId() {
        Document fetchedDocument = DocumentDAO.getDocFromId("nE0IPwAACAAJ");
        assertNotNull(fetchedDocument);
        assertEquals("nE0IPwAACAAJ", fetchedDocument.getId());
        assertEquals("Computer Organization and Architecture", fetchedDocument.getTitle());
    }

    @Test
    void testRemoveDocument() {
        Document newDocument = new Document("", "New Document", "", 3 ,5,
                2, 5.0, 100, "description", "pulisher",
                "publishDate", "https://example.com/new_image.jpg", null,
                null, null);
        DocumentDAO.addDocument(newDocument);
        DocumentDAO.removeDocument(newDocument.getId());
        Document fetchedDocument = DocumentDAO.getDocFromId(newDocument.getId());
        assertNull(fetchedDocument); // Tài liệu phải bị xóa
    }

    @Test
    void testGetAllDoc() {
        ArrayList<Document> documents = DocumentDAO.getAllDoc();
        assertFalse(documents.isEmpty());
        assertTrue(documents.stream().anyMatch(doc -> doc.getId().equals("nE0IPwAACAAJ")));
    }

    @Test
    void testUpdateDocument() {
        Document testDocument = DocumentDAO.getDocFromId("nE0IPwAACAAJ");
        assert testDocument != null;
        testDocument.setTitle("Updated Document");
        DocumentDAO.updateDocument(testDocument);

        Document updatedDocument = DocumentDAO.getDocFromId(testDocument.getId());
        assertNotNull(updatedDocument);
        assertEquals("Updated Document", updatedDocument.getTitle());
    }
}