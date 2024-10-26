package service;

import app.base.Document;
import app.database.DatabaseManagement;
import app.service.GoogleBookAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GoogleBookAPITest {

    @BeforeEach
    public void setUp() {
        DatabaseManagement.setConnection();
    }

    @Test
    public void testGetDocFromId_Valid() throws Exception {
        String validId = "XdMBTKWSfeMC";  // ID hợp lệ
        Document result = GoogleBookAPI.getDocFromId(validId);
        // Kiểm tra kết quả không null
        assertNotNull(result);
        // Kiểm tra các thuộc tính của Document
        assertEquals("Taras Bulba", result.getTitle());
        assertEquals("Ediciones AKAL", result.getPublisher());
        assertEquals("2006-12-19", result.getPublishedDate());
        assertTrue(result.getDescription().startsWith("Feroces, crueles"));
        assertEquals(160, result.getPageCount());
    }
    
}
