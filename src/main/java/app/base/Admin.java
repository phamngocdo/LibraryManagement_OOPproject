package app.base;

import app.dao.DocumentDAO;
import app.dao.ReceiptDAO;
import app.dao.UserDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Admin extends User {
    public Admin(String id, String username, String password, String firstName,
                 String lastName, String birthday, String email, String phoneNumber) {
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

    public void removeMember(String memberId) {
        UserDAO.removeMember(memberId);
    }

    public ArrayList<Receipt> seeAllReceipts() {
        return ReceiptDAO.getAllReceipt();
    }

    public void confirmReturnDocument(Receipt receipt, boolean isReturned) {
        Document doc = DocumentDAO.getDocFromId(receipt.getDocId());
        if (isReturned) {
            receipt.setStatus("returned");
            if (doc != null) {
                doc.setRemaining(doc.getRemaining() + 1);
            }
        } else {
            receipt.setStatus("not returned");
            if (doc != null) {
                doc.setRemaining(doc.getRemaining() - 1);
            }
        }
        ReceiptDAO.updateReceipt(receipt);
        if (doc != null) {
            DocumentDAO.updateDocument(doc);
        }
    }
}
