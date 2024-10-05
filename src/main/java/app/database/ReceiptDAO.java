package app.database;

import app.base.Receipt;
import java.sql.*;
import java.util.ArrayList;

public class ReceiptDAO {
    public static final String MAIN_TABLE = "receipts";

    public static Receipt getReceiptFromId(String receiptId) {
        //

    }

    public static ArrayList<Receipt> getAllReceiptFromMemberId(String memberId) {
        //

    }

    public static ArrayList<Receipt> getAllReceipt() {
        //Phương thức này dùng cho admin khi muốn xem hết phiếu mượn

    }

    public static void addReceipt(Receipt receipt) {
        receipt.setId(DatabaseManagement.createRandomIdInTable(MAIN_TABLE, "receipt_id"));
        //
    }

    public static void removeReceipt(String receiptId) {
        //
    }

    public static void updateReceipt(Receipt receipt) {
        //
    }
}
