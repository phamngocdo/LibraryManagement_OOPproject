package database;

import app.base.Receipt;
import app.database.DatabaseManagement;
import app.database.ReceiptDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ReceiptDAOTest {

    @BeforeEach
    public void setUp() {
        DatabaseManagement.setConnection();
    }

    @Test
    public void testGetReceiptFromId_ValidId() {
        Receipt receipt = ReceiptDAO.getReceiptFromId("receipt123");
        assertNotNull(receipt);
        assertEquals("receipt123", receipt.getId());
    }

    @Test
    public void testGetReceiptFromId_InvalidId() {
        Receipt receipt = ReceiptDAO.getReceiptFromId("invalidId");
        assertNull(receipt);
    }

    @Test
    public void testGetReceiptFromId_NonExistingReceipt() {
        Receipt receipt = ReceiptDAO.getReceiptFromId("nonExistingId");
        assertNull(receipt);
    }

    @Test
    public void testGetAllReceiptFromMemberId_ValidMemberId() {
        ArrayList<Receipt> receipts = ReceiptDAO.getAllReceiptFromMemberId("member123");
        assertNotNull(receipts);
        assertTrue(receipts.size() > 0); // Giả sử có receipts cho member123
    }

    @Test
    public void testGetAllReceiptFromMemberId_InvalidMemberId() {
        ArrayList<Receipt> receipts = ReceiptDAO.getAllReceiptFromMemberId("invalidMember");
        assertNotNull(receipts);
        assertTrue(receipts.isEmpty());
    }

    @Test
    public void testGetAllReceiptFromMemberId_NonExistingMember() {
        ArrayList<Receipt> receipts = ReceiptDAO.getAllReceiptFromMemberId("nonExistingMember");
        assertNotNull(receipts);
        assertTrue(receipts.isEmpty());
    }

    @Test
    public void testGetAllReceipt() {
        ArrayList<Receipt> receipts = ReceiptDAO.getAllReceipt();
        assertNotNull(receipts);
        assertTrue(receipts.size() > 0); // Giả sử có receipts trong bảng
    }

    @Test
    public void testAddReceipt() {
        Receipt receipt = new Receipt(
                "newReceipt", "user123", "doc123",
                LocalDate.of(2024, 6, 10),
                LocalDate.of(2024, 6, 20), "active"
        );
        ReceiptDAO.addReceipt(receipt);
        Receipt retrievedReceipt = ReceiptDAO.getReceiptFromId("newReceipt");
        assertNotNull(retrievedReceipt);
        assertEquals("user123", retrievedReceipt.getUserId());
    }

    @Test
    public void testRemoveReceipt() {
        // Giả sử có một receipt với ID là "removeReceipt"
        ReceiptDAO.removeReceipt("removeReceipt");
        Receipt receipt = ReceiptDAO.getReceiptFromId("removeReceipt");
        assertNull(receipt);
    }

    @Test
    public void testUpdateReceipt() {
        // Giả sử có một receipt với ID là "updateReceipt"
        Receipt originalReceipt = ReceiptDAO.getReceiptFromId("updateReceipt");
        assertNotNull(originalReceipt);

        // Cập nhật thông tin
        originalReceipt.setId("updatedUser");
        ReceiptDAO.updateReceipt(originalReceipt);

        // Kiểm tra xem thông tin đã được cập nhật chưa
        Receipt updatedReceipt = ReceiptDAO.getReceiptFromId("updateReceipt");
        assertNotNull(updatedReceipt);
        assertEquals("updatedUser", updatedReceipt.getUserId());
    }
}
