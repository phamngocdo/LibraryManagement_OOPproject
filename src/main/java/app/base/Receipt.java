package app.base;

import java.time.LocalDate;

public class Receipt {
    private String id;
    private String status;
    private final String userId;
    private final String docId;
    private final LocalDate borrowingDate;
    private final LocalDate returnDate;

    public Receipt(String id, String userId, String docId, LocalDate borrowingDate,
                   LocalDate returnDate, String status){
        this.id = id;
        this.userId = userId;
        this.docId = docId;
        this.borrowingDate = borrowingDate;
        this.returnDate = returnDate;
        this.status = status;
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

    public LocalDate getBorrowingDate() {
        return borrowingDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }
}
