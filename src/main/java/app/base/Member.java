package app.base;

import app.database.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Member extends  User{
    private final ArrayList<Receipt> receipts;

    public Member(String id, String username, String password, String firstName,
                  String lastName, String birthday, String email, String phoneNumber){
        super(id, username, password, firstName, lastName, birthday, email, phoneNumber);
        receipts = ReceiptDAO.getAllReceiptFromMemberId(id);
    }

    public Member(ResultSet resultSet) throws SQLException {
        super(resultSet);
        receipts = ReceiptDAO.getAllReceiptFromMemberId(id);
    }

    public ArrayList<Receipt> getReceipts() {
        return receipts;
    }

    public String register() {
        if (UserDAO.checkUsernameExist(username)) {
            return "Tên đăng nhập đã được sử dụng!";
        }
        UserDAO.addMember(this);
        return "Đăng nhập thành công";
    }

    public void borrowDocument(Document doc) {
        // Tạo receipt có đủ thông tin trong đó status = "not returned", borrowDate = today
        // returnDate + 2 week
        String receiptId = DatabaseManagement.createRandomIdInTable("receipts", "receipt_id");
        LocalDate borrowDate = LocalDate.now();
        LocalDate returnDate = borrowDate.plusWeeks(2);

        // Định dạng ngày thành DD/MM/YYYY
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedBorrowDate = borrowDate.format(formatter);
        String formattedReturnDate = returnDate.format(formatter);

        Receipt receipt = new Receipt(
                receiptId,
                getId(),
                doc.getId(),
                formattedBorrowDate,   // Sử dụng ngày đã định dạng
                formattedReturnDate,   // Sử dụng ngày đã định dạng
                "not returned");

        // Thêm receipt vào danh sách của member
        receipts.add(receipt);

        // Trừ 1 cho remaining của tài liệu
        doc.setRemaining(doc.getRemaining() - 1);

        // Cập nhật vào cơ sở dữ liệu
        ReceiptDAO.addReceipt(receipt); // Thêm receipt vào DB
    }

    public void returnDocument(Receipt receipt) {
        // Cập nhật trạng thái của receipt
        receipt.setStatus("returned");

        // Cộng 1 cho remaining của tài liệu
        Document doc = DocumentDAO.getDocFromId(receipt.getDocId()); // Lấy tài liệu từ DB
        if (doc != null) {
            doc.setRemaining(doc.getRemaining() + 1); // Tăng số lượng tài liệu
        }

        // Cập nhật receipt và tài liệu vào cơ sở dữ liệu
        ReceiptDAO.updateReceipt(receipt); // Cập nhật receipt
        if (doc != null) {
            DocumentDAO.updateDocument(doc); // Cập nhật document
        }
    }

    public void returnDocument(Document doc) {
        Receipt receipt = ReceiptDAO.getReceiptFromDocAndMember(doc.getId(), getId());
        assert receipt != null;
        returnDocument(receipt);
    }

    public void rateDocument(Rating rating) {
        Document doc = DocumentDAO.getDocFromId(rating.getDocId());

        // Cộng 1 cho số lượng người vote và tính lại điểm trung bình
        if (doc != null) {
            double totalRating = doc.getRatingCount() * doc.getAverageScore();
            totalRating += rating.getRatingScore();
            int newRatingCount = doc.getRatingCount() + 1;

            double newAverageRating = totalRating / newRatingCount;

            // Cập nhật lại tài liệu
            doc.setRatingCount(newRatingCount);
            doc.setAverageScore(newAverageRating);

            // Cập nhật tài liệu vào cơ sở dữ liệu
            DocumentDAO.updateDocument(doc);
        }

        // Thêm rating mới vào cơ sở dữ liệu
        RatingDAO.addRating(rating);
    }


    public void updateInfoToDatabase(Member member) {
        UserDAO.updateMember(member);
    }
}
