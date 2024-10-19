package app.base;

import app.database.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class Member extends  User{
    private final ArrayList<Receipt> receipts;

    public Member(String id, String username, String password, String firstName,
                  String lastName, String birthday, String email, String phoneNumber){
        super(id, username, password, firstName, lastName, birthday, email, phoneNumber);
        receipts = ReceiptDAO.getAllReceiptFromMemberId(id);
    }

    public ArrayList<Receipt> getReceipts() {
        return receipts;
    }

    @Override
    public Member signIn(String username, String password) {
        return UserDAO.getMemberFromSignIn(username, password);
    }

    public String signUp(Member member) {
        if (UserDAO.checkUsernameExist(member.getUsername())) {
            return "Username is already taken";
        }
        UserDAO.addMember(member);
        return "Registered successfully";
    }

    public void borrowDocument(Document doc) {
        // Tạo receipt có đủ thông tin trong đó status = "not returned", borrowDate = today
        // returnDate + 2 week
        String receiptId = DatabaseManagement.createRandomIdInTable("receipts", "receipt_id");
        LocalDate borrowDate = LocalDate.now();
        LocalDate returnDate = borrowDate.plusWeeks(2);

        Receipt receipt = new Receipt(receiptId, this.getId(), doc.getId(), borrowDate.toString(), returnDate.toString(), "not returned");

        // Thêm receipt vào danh sách của member
        receipts.add(receipt);

        // Trừ 1 cho remaining của tài liệu
        doc.setRemaining(doc.getRemaining() - 1);

        // Cập nhật vào cơ sở dữ liệu
        ReceiptDAO.addReceipt(receipt); // Thêm receipt vào DB
        DocumentDAO.updateDocument(doc);  // Cập nhật tài liệu
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


    public void rateDocument(Rating rating) {
        Document doc = DocumentDAO.getDocFromId(rating.getDocId());

        // Cộng 1 cho số lượng người vote
        if (doc != null) {
            doc.setRatingCount(doc.getRatingCount() + 1);
            // Cập nhật tài liệu vào cơ sở dữ liệu
            DocumentDAO.updateDocument(doc); // Cập nhật tài liệu
        }

        // Thêm rating vào cơ sở dữ liệu
        RatingDAO.addRating(rating);

    }

    public void updateInfoToDatabase(Member member) {
        UserDAO.updateMember(member);
    }
}
