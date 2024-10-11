package app.database;

import app.base.Document;
import java.sql.*;
import java.util.ArrayList;

public class DocumentDAO {
    public static final String MAIN_TABLE = "documents";

    public static Document getDocFromId(String docId) {
        //

    }
    
    public static ArrayList<Document> getAllDoc() {
        //

    }

    public static void addDocument(Document doc) {
        doc.setId(DatabaseManagement.createRandomIdInTable(MAIN_TABLE, "document_id"));
        //Thêm tài liệu phải thêm cho bảng category và author
    }

    public static void removeDocument(String docId) {
        //xóa tài liệu, receipt, rating tương ứng
    }

    // Kiểm tra xem tài liệu có tồn tại dựa trên tiêu đề
    public static boolean checkDocumentExist(String title) {
        String query = String.format("SELECT * FROM %s WHERE title = '%s'", MAIN_TABLE, title);
        ResultSet resultSet = DatabaseManagement.getResultSetFromQuery(query);
        try {
            return resultSet.next(); // Nếu có kết quả thì tài liệu tồn tại
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Thêm tài liệu.
    public static void updateDocument(Document document) {
        //Xóa doc cũ tương ứng với id sau đó thêm doc mới
    }
}
