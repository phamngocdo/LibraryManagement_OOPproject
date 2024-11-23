package service;

import app.base.Document;
import app.service.GoogleBookAPI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
//All test are correct
public class GoogleBookAPITest {

    @Test
    public void testGetDocFromIsbn_Valid() throws Exception {
        String validIsbn = "9780521679824";  // Isbn hợp lệ
        Document result = GoogleBookAPI.getDocFromIsbn(validIsbn);
        assertNotNull(result);
        assertEquals("Environmental Biology", result.getTitle());
        assertEquals("Cambridge University Press", result.getPublisher());
        assertEquals("2009", result.getPublishedDate());
        assertEquals(689, result.getPageCount());
    }
    
}
