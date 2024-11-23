package app.dao;

import app.base.Receipt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReceiptDAO {

    public static Receipt getReceiptFromId(String receiptId) {
        //kiểm tra receipt_id
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM receipts ");
        query.append(" WHERE receipt_id = ?");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, receiptId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Chuyển đổi ResultSet thành đối tượng Receipt
                return new Receipt(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static ArrayList<Receipt> getAllReceiptFromMemberId(String memberId) {
        //kiểm tra user_id
        ArrayList<Receipt> receipts = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM receipts ");
        query.append("WHERE user_id = ?");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, memberId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // Chuyển đổi ResultSet thành đối tượng Receipt
                receipts.add(new Receipt(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return receipts;
    }

    public static ArrayList<Receipt> getAllReceipt() {
        ArrayList<Receipt> receipts = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM receipts");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(query.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // Chuyển đổi ResultSet thành đối tượng Receipt
                receipts.add(new Receipt(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return receipts;
    }

    public static void addReceipt(Receipt receipt) {
        if (receipt.getId() == null || receipt.getId().isEmpty()) {
            receipt.setId(IdGenerator.createRandomIdInTable("receipts", "receipt_id"));
        }
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO receipts ");
        query.append("(receipt_id, user_id, document_id, borrowing_date, due_date, status) ");
        query.append(" VALUES(?, ?, ?, ?, ?, ?)");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, receipt.getId());
            preparedStatement.setString(2, receipt.getMemberId());
            preparedStatement.setString(3, receipt.getDocId());
            preparedStatement.setString(4, receipt.getBorrowingDate());
            preparedStatement.setString(5, receipt.getDueDate());
            preparedStatement.setString(6, receipt.getStatus());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeReceipt(String receiptId) {
        StringBuilder query = new StringBuilder();
        query.append("DELETE FROM receipts ");
        query.append("WHERE receipt_id = ?");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, receiptId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeReceiptFromMemberId(String userId) {
        StringBuilder query = new StringBuilder();
        query.append("DELETE FROM receipts ");
        query.append("WHERE user_id = ?");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateReceipt(Receipt receipt) {
        //Cập nhật receipt bằng cách gọi hàm remove(id) sau đó addReceipt
        removeReceipt(receipt.getId());
        addReceipt(receipt);
    }
}
