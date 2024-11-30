package database;

import app.base.Receipt;
import app.dao.ReceiptDAO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

//All Test are correct
public class ReceiptDAOTest {

    @Test
    public void testGetReceiptFromId_ValidId() {
        Receipt receipt = ReceiptDAO.getReceiptFromId("I2HvbHx1GE5j");
        assertNotNull(receipt);
        assertEquals("OJIK98JHNTMT", receipt.getMemberId());
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
        assertFalse(receipts.isEmpty());
    }

    @Test
    public void testGetAllReceipt() {
        ArrayList<Receipt> receipts = ReceiptDAO.getAllReceipt();
        assertNotNull(receipts);
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
        assertEquals("OJIK98JHNTMT", retrievedReceipt.getMemberId());
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
        Receipt originalReceipt = ReceiptDAO.getReceiptFromId("ItT8OEFbQafH");
        assertNotNull(originalReceipt);

        originalReceipt.setStatus("returned");
        ReceiptDAO.updateReceipt(originalReceipt);

        Receipt updatedReceipt = ReceiptDAO.getReceiptFromId("ItT8OEFbQafH");
        assertNotNull(updatedReceipt);
        assertEquals("returned", updatedReceipt.getStatus());
    }
}
