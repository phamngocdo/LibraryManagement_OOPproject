package database;

import app.base.Receipt;
import app.dao.DatabaseManagement;
import app.dao.ReceiptDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

//All Test are correct
public class ReceiptDAOTest {

    @BeforeEach
    void setUp() {
        DatabaseManagement.setConnection();
    }

    @Test
    public void testGetReceiptFromId_ValidId() {
        Receipt receipt = ReceiptDAO.getReceiptFromId("USHXJWYOKG4N");
        assertNotNull(receipt);
        //kiểm tra user_id có đúng k
        assertEquals("OJIK98JHNTMT", receipt.getUserId());
    }

    @Test
    public void testGetReceiptFromId_NonExistingReceipt() {
        Receipt receipt = ReceiptDAO.getReceiptFromId("999999999999");
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
                "", "OJIK98JHNTMT", "VKATH_VZXCUC",
                "10/06/2024",
                "24/06/2024", "not returned"
        );
        ReceiptDAO.addReceipt(receipt);
        Receipt retrievedReceipt = ReceiptDAO.getReceiptFromId(receipt.getId());
        assertNotNull(retrievedReceipt);
        assertEquals("OJIK98JHNTMT", retrievedReceipt.getUserId());
    }

    @Test
    public void testRemoveReceipt() {
        Receipt receiptToRemove = new Receipt(
                "", "OJIK98JHNTMT", "LG5D4ruJCeMC",
                "10/06/2024",
                "24/06/2024", "not returned"
        );
        ReceiptDAO.addReceipt(receiptToRemove);
        ReceiptDAO.removeReceipt(receiptToRemove.getId());
        Receipt receipt = ReceiptDAO.getReceiptFromId(receiptToRemove.getId());
        assertNull(receipt);
    }

    @Test
    public void testUpdateReceipt() {
        //lấy receipt id trong db có status là not return
        Receipt originalReceipt = ReceiptDAO.getReceiptFromId("4V9KMV8NO0M4");
        assertNotNull(originalReceipt);

        // Cập nhật thông tin
        originalReceipt.setStatus("returned");
        ReceiptDAO.updateReceipt(originalReceipt);

        // Kiểm tra xem thông tin đã được cập nhật chưa
        Receipt updatedReceipt = ReceiptDAO.getReceiptFromId("4V9KMV8NO0M4");
        assertNotNull(updatedReceipt);
        assertEquals("returned", updatedReceipt.getStatus());
    }
}
