package app.base;

import app.dao.DocumentDAO;
import app.dao.ReceiptDAO;
import app.dao.UserDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    public void confirmReturnDocument(Receipt receipt) {
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
}
