package app.base;

import app.dao.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Member extends User {
    private final ArrayList<Receipt> receipts;

    public Member(String id, String username, String password, String firstName,
                  String lastName, String birthday, String email, String phoneNumber) {
        super(id, username, password, firstName, lastName, birthday, email, phoneNumber);
        receipts = ReceiptDAO.getAllReceiptFromMemberId(id);
    }

    public Member(ResultSet resultSet) throws SQLException {
        super(resultSet);
        receipts = ReceiptDAO.getAllReceiptFromMemberId(id);
    }

    public static Member loadFromId(String id) {
        return UserDAO.getMemberFromId(id);
    }

    public ArrayList<Receipt> getReceipts() {
        return receipts;
    }

    public String register() {
        if (UserDAO.checkUsernameExist(username)) {
            return "Tên đăng nhập đã được sử dụng!";
        }
        UserDAO.addMember(this);
        return "Đăng ký thành công";
    }

    public void borrowDocument(Document doc) {
        String receiptId = IdGenerator.createRandomIdInTable("receipts", "receipt_id");
        LocalDate borrowDate = LocalDate.now();
        LocalDate returnDate = borrowDate.plusWeeks(2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedBorrowDate = borrowDate.format(formatter);
        String formattedReturnDate = returnDate.format(formatter);

        Receipt receipt = new Receipt(
                receiptId,
                getId(),
                doc.getId(),
                formattedBorrowDate,
                formattedReturnDate,
                "not returned");
        receipts.add(receipt);
        doc.setRemaining(doc.getRemaining() - 1);
        DocumentDAO.updateDocument(doc);
        ReceiptDAO.addReceipt(receipt);
    }

    public void rateDocument(Rating rating, Document doc) {
        double totalRating = doc.getRatingCount() * doc.getAverageScore();
        totalRating += rating.getRatingScore();
        int newRatingCount = doc.getRatingCount() + 1;
        double newAverageRating = totalRating / newRatingCount;
        doc.setRatingCount(newRatingCount);
        doc.setAverageScore(newAverageRating);
        doc.getRatings().add(rating);
        DocumentDAO.updateDocument(doc);
        RatingDAO.addRating(rating);
    }

    public static void updateInfoToDatabase(Member member) {
        UserDAO.updateMember(member);
    }
}
