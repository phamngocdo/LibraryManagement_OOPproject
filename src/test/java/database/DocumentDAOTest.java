package database;

import app.base.Document;
import app.database.DatabaseManagement;
import app.database.DocumentDAO;
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
        Document newDocument = new Document("", "New Document", "0987654321", 3,5,
                2, 5.6, "https://example.com/new_image.jpg");
        DocumentDAO.addDocument(newDocument);
        Document fetchedDocument = DocumentDAO.getDocFromId(newDocument.getId());
        assertNotNull(fetchedDocument);
        assertEquals("New Document", fetchedDocument.getTitle());
        assertEquals("0987654321", fetchedDocument.getIsbn());
    }

    @Test
    void testGetDocFromId() {
        Document fetchedDocument = DocumentDAO.getDocFromId("7Q0TFMI7FIKC");
        assertNotNull(fetchedDocument);
        assertEquals("7Q0TFMI7FIKC", fetchedDocument.getId());
        assertEquals("Beginning Linux?Programming", fetchedDocument.getTitle());
    }

    @Test
    void testRemoveDocument() {
        Document newDocument = new Document("", "New Document", "0987654321", 3,5,
                2, 5.6, "https://example.com/new_image.jpg");
        DocumentDAO.addDocument(newDocument);
        DocumentDAO.removeDocument(newDocument.getId());
        Document fetchedDocument = DocumentDAO.getDocFromId(newDocument.getId());
        assertNull(fetchedDocument); // Tài liệu phải bị xóa
    }

    @Test
    void testGetAllDoc() {
        ArrayList<Document> documents = DocumentDAO.getAllDoc();
        assertFalse(documents.isEmpty());
        assertTrue(documents.stream().anyMatch(doc -> doc.getId().equals("7Q0TFMI7FIKC")));
    }

    @Test
    void testUpdateDocument() {
        Document testDocument = DocumentDAO.getDocFromId("7Q0TFMI7FIKC");
        assert testDocument != null;
        testDocument.setTitle("Updated Document");
        DocumentDAO.updateDocument(testDocument);

        Document updatedDocument = DocumentDAO.getDocFromId(testDocument.getId());
        assertNotNull(updatedDocument);
        assertEquals("Updated Document", updatedDocument.getTitle());
    }
}