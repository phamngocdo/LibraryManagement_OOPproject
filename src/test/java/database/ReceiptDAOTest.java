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
        Receipt receipt = ReceiptDAO.getReceiptFromId("Q7ND2HK1FJ3L");
        assertNotNull(receipt);
        //kiểm tra user_id có đúng k
        assertEquals("OJIK98JHNTMT", receipt.getUserId());
    }

    @Test
    public void testGetReceiptFromId_InvalidId() {
        Receipt receipt = ReceiptDAO.getReceiptFromId("invalid-id");
        assertNull(receipt);
    }

    @Test
    public void testGetReceiptFromId_NonExistingReceipt() {
        Receipt receipt = ReceiptDAO.getReceiptFromId("non-id");
        assertNull(receipt);
    }

    @Test
    public void testGetAllReceiptFromMemberId_ValidMemberId() {
        ArrayList<Receipt> receipts = ReceiptDAO.getAllReceiptFromMemberId("OJIK98JHNTMT");
        assertNotNull(receipts);
        //kiểm tra danh sách receipts có trống k
        assertFalse(receipts.isEmpty());
    }

    @Test
    public void testGetAllReceipt() {
        ArrayList<Receipt> receipts = ReceiptDAO.getAllReceipt();
        assertNotNull(receipts);
        //kiểm tra danh sách receipts có trống k
        assertFalse(receipts.isEmpty());
    }

    @Test
    public void testAddReceipt() {
        Receipt receipt = new Receipt(
                "newreceipt", "newuser", "newdoc",
                LocalDate.of(2024, 6, 10),
                LocalDate.of(2024, 6, 20), "not returned"
        );
        ReceiptDAO.addReceipt(receipt);
        Receipt retrievedReceipt = ReceiptDAO.getReceiptFromId("newreceipt");
        assertNotNull(retrievedReceipt);
        assertEquals("newuser", retrievedReceipt.getUserId());
    }

    @Test
    public void testRemoveReceipt() {
        Receipt receiptToRemove = new Receipt(
                "newreceipt1", "newuser", "newdoc",
                LocalDate.of(2024, 6, 10),
                LocalDate.of(2024, 6, 20), "not returned"
        );
        ReceiptDAO.addReceipt(receiptToRemove);
        ReceiptDAO.removeReceipt("newreceipt1");
        Receipt receipt = ReceiptDAO.getReceiptFromId("newreceipt1");
        assertNull(receipt);
    }

    @Test
    public void testUpdateReceipt() {
        //lấy receipt id trong db có status là not return
        Receipt originalReceipt = ReceiptDAO.getReceiptFromId("F7P3R9K2YJLM");
        assertNotNull(originalReceipt);

        // Cập nhật thông tin
        originalReceipt.setStatus("return");
        ReceiptDAO.updateReceipt(originalReceipt);

        // Kiểm tra xem thông tin đã được cập nhật chưa
        Receipt updatedReceipt = ReceiptDAO.getReceiptFromId("F7P3R9K2YJLM");
        assertNotNull(updatedReceipt);
        assertEquals("return", updatedReceipt.getStatus());
    }
}
