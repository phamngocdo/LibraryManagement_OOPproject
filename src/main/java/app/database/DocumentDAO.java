package app.database;

import app.base.Author;
import app.base.Category;
import app.base.Document;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static app.database.AuthorDAO.DOCUMENT_AUTHOR_TABLE;
import static app.database.CategoryDAO.DOCUMENT_CATEGORY_TABLE;

public class DocumentDAO {
    public static final String MAIN_TABLE = "documents";
    public static final String RATINGS_TABLE = "ratings";
    public static final String RECEIPTS_TABLE = "receipts";
    public static final String AUTHORS_TABLE = "authors";
    public static final String CATEGORIES_TABLE = "categories";

    // Tìm kiếm tài lịu khi biết Id.
    public static Document getDocFromId(String docId) {
        String query = String.format("SELECT * FROM %s WHERE document_id = '%s'", MAIN_TABLE, docId);
        ResultSet resultSet = DatabaseManagement.getResultSetFromQuery(query);
        try {
            if (resultSet.next()) {
                return new Document(
                        resultSet.getString("document_id"),
                        resultSet.getString("title"),
                        resultSet.getString("isbn"),
                        resultSet.getInt("quantity"),
                        resultSet.getInt("remaining"),
                        resultSet.getInt("ratings_count"),
                        resultSet.getDouble("average_rating"),
                        resultSet.getString("image_url")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // Lấy tất cả tài liệu.
    public static ArrayList<Document> getAllDoc() {
        ArrayList<Document> documents = new ArrayList<>();
        String query = String.format("SELECT * FROM %s", MAIN_TABLE);
        ResultSet resultSet = DatabaseManagement.getResultSetFromQuery(query);
        try {
            while (resultSet.next()) {
                documents.add(new Document(
                        resultSet.getString("document_id"),
                        resultSet.getString("title"),
                        resultSet.getString("isbn"),
                        resultSet.getInt("quantity"),
                        resultSet.getInt("remaining"),
                        resultSet.getInt("ratings_count"),
                        resultSet.getDouble("average_rating"),
                        resultSet.getString("image_url")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return documents;
    }

    // Lấy tất cả tài liệu cùng tác giả.
    public static ArrayList getAllDocumentFromAuthor(String authorName) {
        ArrayList<Document> documents = new ArrayList<>();
        String query = String.format(
                "SELECT d.* FROM %s AS d " +
                        "JOIN %s AS da ON d.document_id = da.document_id " +
                        "JOIN %s AS a ON da.author_id = a.author_id " +
                        "WHERE a.name = '%s'",
                MAIN_TABLE, DOCUMENT_AUTHOR_TABLE, AUTHORS_TABLE, authorName
        );

        ResultSet resultSet = DatabaseManagement.getResultSetFromQuery(query);
        try {
            while (resultSet.next()) {
                documents.add(new Document(
                        resultSet.getString("document_id"),
                        resultSet.getString("title"),
                        resultSet.getString("isbn"),
                        resultSet.getInt("quantity"),
                        resultSet.getInt("remaining"),
                        resultSet.getInt("ratings_count"),
                        resultSet.getDouble("average_rating"),
                        resultSet.getString("image_url")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return documents;
    }

    // Lấy ra các tài liệu cùng thể loai.
    public static ArrayList getAllDocumentFromCategory(String category) {
        ArrayList<Document> documents = new ArrayList<>();
        String query = String.format(
                "SELECT d.* FROM %s AS d " +
                        "JOIN %s AS dc ON d.document_id = dc.document_id " +
                        "JOIN %s AS c ON dc.category_id = c.category_id " +
                        "WHERE c.category = '%s'",
                MAIN_TABLE, DOCUMENT_CATEGORY_TABLE, CATEGORIES_TABLE, category
        );

        ResultSet resultSet = DatabaseManagement.getResultSetFromQuery(query);
        try {
            while (resultSet.next()) {
                documents.add(new Document(
                        resultSet.getString("document_id"),
                        resultSet.getString("title"),
                        resultSet.getString("isbn"),
                        resultSet.getInt("quantity"),
                        resultSet.getInt("remaining"),
                        resultSet.getInt("ratings_count"),
                        resultSet.getDouble("average_rating"),
                        resultSet.getString("image_url")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return documents;
    }


    // Lấy ra number tác phẩm có điểm đánh giá cao nhất.
    public static ArrayList getBestRatingDocuments(int number) {
        ArrayList<Document> documents = new ArrayList<>();
        String query = String.format(
                "SELECT * FROM %s ORDER BY average_rating DESC LIMIT %d",
                MAIN_TABLE, number
        );

        ResultSet resultSet = DatabaseManagement.getResultSetFromQuery(query);
        try {
            while (resultSet.next()) {
                documents.add(new Document(
                        resultSet.getString("document_id"),
                        resultSet.getString("title"),
                        resultSet.getString("isbn"),
                        resultSet.getInt("quantity"),
                        resultSet.getInt("remaining"),
                        resultSet.getInt("ratings_count"),
                        resultSet.getDouble("average_rating"),
                        resultSet.getString("image_url")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return documents;
    }

    // Thêm tài liệu.
    public static void addDocument(Document doc) {
        doc.setId(DatabaseManagement.createRandomIdInTable(MAIN_TABLE, "document_id"));
        //Thêm tài liệu phải thêm cho bảng category và author
        String query = String.format(
                "INSERT INTO %s (document_id, title, isbn, quantity, remaining, ratings_count, " +
                        "average_rating, image_url) VALUES ('%s', '%s', '%s', %d, %d, %d, %f, '%s')",
                MAIN_TABLE,
                doc.getId(),
                doc.getTitle(),
                doc.getIsbn(),
                doc.getQuantity(),
                doc.getRemaining(),
                doc.getRatingCount(),
                doc.getAverageScore(),
                doc.getImageUrl()
        );
        DatabaseManagement.executeUpdate(query);
        // Thêm vào bảng document_category
        for (Category category : doc.getCategories()) {
            String categoryId = category.getId();
            String categoryQuery = String.format(
                    "INSERT INTO %s (document_id, category_id) VALUES ('%s', '%s')",
                    DOCUMENT_CATEGORY_TABLE, doc.getId(), categoryId
            );
            DatabaseManagement.executeUpdate(categoryQuery);
        }

        // Thêm vào bảng document_author
        for (Author author : doc.getAuthors()) {
            String authorId = author.getId();
            String authorQuery = String.format(
                    "INSERT INTO %s (document_id, author_id) VALUES ('%s', '%s')",
                    DOCUMENT_AUTHOR_TABLE, doc.getId(), authorId
            );
            DatabaseManagement.executeUpdate(authorQuery);
        }
    }

    // Xóa tài liệu.
    public static void removeDocument(String docId) {
        String query = String.format("DELETE FROM %s WHERE document_id = '%s'", MAIN_TABLE, docId);
        DatabaseManagement.executeUpdate(query);

        // Xóa bản ghi liên quan trong bảng document_author
        String authorQuery = String.format("DELETE FROM %s WHERE document_id = '%s'",
                DOCUMENT_AUTHOR_TABLE,
                docId
        );
        DatabaseManagement.executeUpdate(authorQuery);

        // Xóa bản ghi liên quan trong bảng document_category
        String categoryQuery = String.format("DELETE FROM %s WHERE document_id = '%s'",
                DOCUMENT_CATEGORY_TABLE,
                docId
        );
        DatabaseManagement.executeUpdate(categoryQuery);

        // Xóa bản ghi liên quan trong bảng receipts
        String deleteReceiptsQuery = String.format("DELETE FROM '%s' WHERE document_id = '%s'",
                RECEIPTS_TABLE,
                docId
        );
        DatabaseManagement.executeUpdate(deleteReceiptsQuery);

        // Xóa bản ghi liên quan trong bảng ratings
        String deleteRatingsQuery = String.format("DELETE FROM '%s' WHERE document_id = '%s'",
                RATINGS_TABLE,
                docId
        );
        DatabaseManagement.executeUpdate(deleteRatingsQuery);
    }

    public static void updateDocument(Document document) {
        //Xóa doc cũ tương ứng với id sau đó thêm doc mới
        removeDocument(document.getId());
        addDocument(document);
    }
}