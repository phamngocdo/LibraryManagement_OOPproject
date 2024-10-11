package database;

import app.base.Author;
import app.base.Category;
import app.base.Document;
import app.database.DatabaseManagement;
import app.database.DocumentDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentDAOTest {

    private Document testDocument;

    @BeforeEach
    void setUp() {
        DatabaseManagement.setConnection();
    }

    @Test
    void testAddDocument() {
        Document newDocument = new Document("doc2", "New Document", "0987654321", 3,5,
                2, 5.6, "http://example.com/new_image.jpg");
        DocumentDAO.addDocument(newDocument);

        Document fetchedDocument = DocumentDAO.getDocFromId(newDocument.getId());
        assertNotNull(fetchedDocument);
        assertEquals("New Document", fetchedDocument.getTitle());
        assertEquals("0987654321", fetchedDocument.getIsbn());
    }

    @Test
    void testGetDocFromId() {
        Document fetchedDocument = DocumentDAO.getDocFromId(testDocument.getId());
        assertNotNull(fetchedDocument);
        assertEquals(testDocument.getId(), fetchedDocument.getId());
        assertEquals(testDocument.getTitle(), fetchedDocument.getTitle());
    }

    @Test
    void testRemoveDocument() {
        DocumentDAO.removeDocument(testDocument.getId());

        Document fetchedDocument = DocumentDAO.getDocFromId(testDocument.getId());
        assertNull(fetchedDocument); // Tài liệu phải bị xóa
    }

    @Test
    void testGetAllDoc() {
        ArrayList<Document> documents = DocumentDAO.getAllDoc();
        assertFalse(documents.isEmpty());
        assertTrue(documents.stream().anyMatch(doc -> doc.getId().equals(testDocument.getId())));
    }

    @Test
    void testUpdateDocument() {
        testDocument.setTitle("Updated Document");
        DocumentDAO.updateDocument(testDocument);

        Document updatedDocument = DocumentDAO.getDocFromId(testDocument.getId());
        assertNotNull(updatedDocument);
        assertEquals("Updated Document", updatedDocument.getTitle());
    }
}