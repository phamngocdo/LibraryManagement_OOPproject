package app.base;

import app.database.ReceiptDAO;
import app.database.UserDAO;

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
        // returnDate = borrowDate + 2 tuần
        // receipts.add(receipt)
        // trừ 1 cho remaining của doc
        //RatingDAO.add
        //DocumentDAO.update
    }

    public void returnDocument(Receipt receipt) {
        // receipt.setStatus("returned")
        // cộng 1 cho remaining
        // ReceiptDAO.update
        // DocumentDAO.update
    }

    public void rateDocument(Rating rating) {
         /* Document doc = getDocFromId()
         * doc.updateRating()
         * DocumentDAO.update
         * RatingDAO.add
         */
    }

    public void updateInfoToDatabase(Member member) {
        UserDAO.updateMember(member);
    }
}
