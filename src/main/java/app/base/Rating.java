package app.base;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Rating {
    private String id;
    private final String memberId;
    private final String docId;
    private final int ratingScore;
    private final String comment;

    public Rating(String id, String memberId, String docId, int ratingScore, String comment) {
        this.id = id;
        this.memberId = memberId;
        this.docId = docId;
        this.ratingScore = ratingScore;
        this.comment = comment;
    }

    public Rating(ResultSet resultSet) throws SQLException {
        id = resultSet.getString("rating_id");
        memberId = resultSet.getString("user_id");
        docId = resultSet.getString("document_id");
        ratingScore = resultSet.getInt("rating_score");
        comment = resultSet.getString("comment");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getDocId() {
        return docId;
    }

    public int getRatingScore() {
        return ratingScore;
    }

    public String getComment() {
        return comment;
    }
}
