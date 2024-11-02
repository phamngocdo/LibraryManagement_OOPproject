package app.base;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Receipt {
    private String id;
    private String status;
    private final String userId;
    private final String docId;
    private final String borrowingDate;
    private final String returnDate;

    public Receipt(String id, String userId, String docId, String borrowingDate,
                   String returnDate, String status){
        this.id = id;
        this.userId = userId;
        this.docId = docId;
        this.borrowingDate = borrowingDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public Receipt(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getString("receipt_id");
        this.userId = resultSet.getString("user_id");
        this.docId = resultSet.getString("document_id");
        this.borrowingDate = resultSet.getString("borrowing_date");
        this.returnDate = resultSet.getString("return_date");
        this.status = resultSet.getString("status");
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    public String getDocId() {
        return docId;
    }

    public String getBorrowingDate() {
        return borrowingDate;
    }

    public String getReturnDate() {
        return returnDate;
    }
}
