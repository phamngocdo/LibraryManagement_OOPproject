package service;

import app.service.GoogleBookAPI;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class GoogleBookAPITest {

    @Test
    public void testGetDocFromId_Valid() throws Exception {
        String validId = "XdMBTKWSfeMC";
        HashMap<String, String> result = GoogleBookAPI.getDocFromId(validId);

        assertNotNull(result); //kiểm tra null k
        assertEquals("Ediciones AKAL", result.get("publisher")); //kiểm tra publisher
        assertEquals("2006-12-19", result.get("publishedDate"));
        assertTrue(result.get("description").startsWith("Feroces, crueles"));
        assertEquals("160", result.get("pageCount"));
        assertEquals("es", result.get("language"));
    }

    @Test
    public void testGetDocFromId_Invalid() {
        String invalidId = "INVALID_ID";

        Exception exception = assertThrows(Exception.class, () -> {
            GoogleBookAPI.getDocFromId(invalidId);//bắt ngoại lệ
        });

        String expectedMessage = "503 - Service temporarily unavailable."; //mesage lỗi mong đợi
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage)); //kiểm tra mesage lỗi có giống k
    }
}
