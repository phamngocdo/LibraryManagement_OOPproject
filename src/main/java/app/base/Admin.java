package app.base;

import app.database.DocumentDAO;
import app.database.RatingDAO;
import app.database.ReceiptDAO;
import app.database.UserDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Admin  extends User{
    public Admin(String id, String username, String password, String firstName,
                String lastName, String birthday, String email, String phoneNumber){
        super(id, username, password, firstName, lastName, birthday, email, phoneNumber);
    }

    public Admin(ResultSet resultSet) throws SQLException {
        super(resultSet);
    }

    public String addDocument(Document doc) {
        if (DocumentDAO.checkDocumentExist(doc.getId())) {
            return "Tài liệu đã tồn tại";
        }
        DocumentDAO.addDocument(doc);
        return "Thêm tài liệu thành công";
    }

    public String removeDocument(String docId) {
        DocumentDAO.removeDocument(docId);
        return "Xóa tài liệu thành công";
    }

    public String updateDocument(Document doc) {
        DocumentDAO.updateDocument(doc);
        return "Cập nhật tài liệu thành công";
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
