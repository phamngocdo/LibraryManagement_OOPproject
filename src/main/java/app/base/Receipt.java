package app.base;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Receipt {
    private String id;
    private String status;
    private final String memberId;
    private final String docId;
    private final String borrowingDate;
    private final String dueDate;

    public Receipt(String id, String memberId, String docId, String borrowingDate,
                   String dueDate, String status) {
        this.id = id;
        this.memberId = memberId;
        this.docId = docId;
        this.borrowingDate = borrowingDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    public Receipt(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getString("receipt_id");
        this.memberId = resultSet.getString("user_id");
        this.docId = resultSet.getString("document_id");
        this.borrowingDate = resultSet.getString("borrowing_date");
        this.dueDate = resultSet.getString("due_date");
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

    public String getMemberId() {
        return memberId;
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

    public String getDueDate() {
        return dueDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Receipt receipt = (Receipt) obj;

        return id.equals(receipt.id) &&
                memberId.equals(receipt.memberId) &&
                docId.equals(receipt.docId) &&
                borrowingDate.equals(receipt.borrowingDate) &&
                dueDate.equals(receipt.dueDate) &&
                status.equals(receipt.status);
    }
}
