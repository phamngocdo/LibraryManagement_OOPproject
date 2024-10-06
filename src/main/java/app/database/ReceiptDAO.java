package app.database;

import app.base.Receipt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ReceiptDAO {
    public static final String MAIN_TABLE = "receipts";

    public static Receipt getReceiptFromId(String receiptId) {
        //kiểm tra receipt_id
        String query = String.format("SELECT * FROM %s WHERE receipt_id = '%s'", MAIN_TABLE, receiptId);
        ResultSet resultSet = DatabaseManagement.getResultSetFromQuery(query);
        try {
            if (resultSet.next()) {
                //lấy borrowing_date từ db chuyển thành LocalDate
                String borrowingDateString = resultSet.getString("borrowing_date");
                LocalDate borrowingDate = LocalDate.parse(
                        borrowingDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                //lấy return_date từ db chuyển thành LocalDate
                String returnDateString = resultSet.getString("return_date");
                LocalDate returnDate = LocalDate.parse(
                        returnDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                // Chuyển đổi ResultSet thành đối tượng Receipt
                return new Receipt(
                        resultSet.getString("receipt_id"),
                        resultSet.getString("user_id"),
                        resultSet.getString("document_id"),
                        borrowingDate,
                        returnDate,
                        resultSet.getString("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Receipt> getAllReceiptFromMemberId(String memberId) {
        //kiểm tra user_id
        ArrayList<Receipt> receipts = new ArrayList<>();
        String query = String.format("SELECT * FROM %s WHERE user_id = '%s'", MAIN_TABLE, memberId);
        ResultSet resultSet = DatabaseManagement.getResultSetFromQuery(query);
        try {
            while (resultSet.next()) {
                //lấy borrowing_date từ db chuyển thành LocalDate
                String borrowingDateString = resultSet.getString("borrowing_date");
                LocalDate borrowingDate = LocalDate.parse(
                        borrowingDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                //lấy return_date từ db chuyển thành LocalDate
                String returnDateString = resultSet.getString("return_date");
                LocalDate returnDate = LocalDate.parse(
                        returnDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                // Chuyển đổi ResultSet thành đối tượng Receipt
                receipts.add(new Receipt(
                        resultSet.getString("receipt_id"),
                        resultSet.getString("member_id"),
                        resultSet.getString("doc_id"),
                        borrowingDate,
                        returnDate,
                        resultSet.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return receipts;
    }

    public static ArrayList<Receipt> getAllReceipt() {
        //Phương thức này dùng cho admin khi muốn xem hết phiếu mượn
        //trả về table receipts
        ArrayList<Receipt> receipts = new ArrayList<>();
        String query = String.format("SELECT * FROM %s", MAIN_TABLE);
        ResultSet resultSet = DatabaseManagement.getResultSetFromQuery(query);
        try {
            while (resultSet.next()) {
                //lấy borrowing_date từ db chuyển thành LocalDate
                String borrowingDateString = resultSet.getString("borrowing_date");
                LocalDate borrowingDate = LocalDate.parse(
                        borrowingDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                //lấy return_date từ db chuyển thành LocalDate
                String returnDateString = resultSet.getString("return_date");
                LocalDate returnDate = LocalDate.parse(
                        returnDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                // Chuyển đổi ResultSet thành đối tượng Receipt
                receipts.add(new Receipt(
                        resultSet.getString("receipt_id"),
                        resultSet.getString("member_id"),
                        resultSet.getString("doc_id"),
                        borrowingDate,
                        returnDate,
                        resultSet.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return receipts;
    }

    public static void addReceipt(Receipt receipt) {
        receipt.setId(DatabaseManagement.createRandomIdInTable(MAIN_TABLE, "receipt_id"));
        //Chuyển LocalDate borrowingDate, returnDate sang string dạng yyyy-MM-dd
        String borrowingDateString = receipt.getBorrowingDate().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String returnDateString = receipt.getReturnDate().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String query = String.format(
                "INSERT INTO %s (receipt_id, user_id, document_id, borrowing_date, return_date, status) " +
                        "VALUES ('%s', '%s', '%s', '%s', '%s', '%s')",
                MAIN_TABLE,
                receipt.getId(),
                receipt.getUserId(),
                receipt.getDocId(),
                borrowingDateString,
                returnDateString,
                receipt.getStatus()
        );
        DatabaseManagement.executeUpdate(query);
    }

    public static void removeReceipt(String receiptId) {
        //xóa receipt
        String query = String.format("DELETE FROM %s WHERE receipt_id = '%s'", MAIN_TABLE, receiptId);
        DatabaseManagement.executeUpdate(query);
    }

    public static void updateReceipt(Receipt receipt) {
        //Cập nhật receipt bằng cách gọi hàm remove(id) sau đó addReceipt
        removeReceipt(receipt.getId());
        addReceipt(receipt);
    }
}
