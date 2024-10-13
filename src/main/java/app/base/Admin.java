package app.base;

import app.database.DocumentDAO;
import app.database.RatingDAO;
import app.database.ReceiptDAO;
import app.database.UserDAO;

import java.time.LocalDate;
import java.util.ArrayList;

public class Admin  extends User{
    public Admin(String id, String username, String password, String firstName,
                String lastName, String birthday, String email, String phoneNumber){
        super(id, username, password, firstName, lastName, birthday, email, phoneNumber);
    }

    @Override
    public boolean signIn(String username, String password) {
        // kiểm tra role = admin không//
        return super.signIn(username, password); //Thêm && role = "admin"//
    }

    public void addDocument(Document doc) {
        DocumentDAO.addDocument(doc);
    }

    public void removeDocument(String docId) {
        DocumentDAO.removeDocument(docId);
    }

    public void updateDocument(Document doc) {
        DocumentDAO.updateDocument(doc);
    }

    public ArrayList<Member> seeAllMemberInfo() {
        return UserDAO.getAllMember();
    }

    public ArrayList<Receipt> seeAllReceipts() {
        return ReceiptDAO.getAllReceipt();
    }

    public ArrayList<Rating> seeAllRating() {
        return RatingDAO.getAllRating();
    }
}
