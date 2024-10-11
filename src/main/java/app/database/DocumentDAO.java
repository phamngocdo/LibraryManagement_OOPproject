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

    public static void updateDocument(Document document) {
        //Xóa doc cũ tương ứng với id sau đó thêm doc mới
    }
}
