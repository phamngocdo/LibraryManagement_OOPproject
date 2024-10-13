package app.database;
import app.base.Author;
import app.base.Category;
import app.base.Document;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DocumentDAO {

    // Tìm kiếm tài lịu khi biết Id.
    public static Document getDocFromId(String docId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM documents ");
        query.append(" WHERE document_id = ?");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, docId);
            ResultSet resultSet = preparedStatement.executeQuery();
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
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM documents");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getConnection().prepareStatement(query.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
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
    public static ArrayList<Document> getAllDocumentFromAuthor(String authorName) {
        ArrayList<Document> documents = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT d.* FROM documents AS d ");
        query.append("JOIN document_author AS da ON d.document_id = da.document_id ");
        query.append("JOIN authors AS a ON da.author_id = a.author_id ");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getConnection().prepareStatement(query.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
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
    public static ArrayList<Document> getAllDocumentFromCategory(String category) {
        ArrayList<Document> documents = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT d.* FROM documents AS d ");
        query.append("JOIN document_category AS dc ON d.document_id = dc.document_id ");
        query.append("JOIN categories AS c ON dc.category_id = c.category_id ");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getConnection().prepareStatement(query.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
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
    public static ArrayList<Document> getBestRatingDocuments(int number) {
        ArrayList<Document> documents = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM documents ORDER BY average_rating DESC LIMIT ?");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getConnection().prepareStatement(query.toString());
            preparedStatement.setInt(1, number);
            ResultSet resultSet = preparedStatement.executeQuery();
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
        if (doc.getId().isEmpty()) {
            doc.setId(DatabaseManagement.createRandomIdInTable("documents", "document_id"));
        }
        //Thêm tài liệu phải thêm cho bảng category và author
        StringBuilder docQuery = new StringBuilder();
        docQuery.append("INSERT INTO documents ");
        docQuery.append("(document_id, title, isbn, quantity, remaining, ratings_count, average_rating, image_url) ");
        docQuery.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getConnection().prepareStatement(docQuery.toString());
            preparedStatement.setString(1, doc.getId());
            preparedStatement.setString(2, doc.getTitle());
            preparedStatement.setString(3, doc.getIsbn());
            preparedStatement.setInt(4, doc.getQuantity());
            preparedStatement.setInt(5, doc.getRemaining());
            preparedStatement.setInt(6, doc.getRatingCount());
            preparedStatement.setDouble(7, doc.getAverageScore());
            preparedStatement.setString(8, doc.getImageUrl());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Thêm vào bảng document_category
        for (Category category : doc.getCategories()) {
            StringBuilder categoryDocQuery = new StringBuilder();
            categoryDocQuery.append("INSERT INTO document_category (document_id, category_id)");
            categoryDocQuery.append(" VALUES (?, ?)");
            try {
                PreparedStatement preparedStatement;
                preparedStatement = DatabaseManagement.getConnection().prepareStatement(categoryDocQuery.toString());
                preparedStatement.setString(1, doc.getId());
                preparedStatement.setString(2, category.getId());
                preparedStatement.executeUpdate();

                if (!CategoryDAO.checkCategoryExist(category.getCategory())) {
                    CategoryDAO.addCategory(category);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        // Thêm vào bảng document_author
        for (Author author : doc.getAuthors()) {
            StringBuilder authorDocQuery = new StringBuilder();
            authorDocQuery.append("INSERT INTO document_author (document_id, author_id)");
            authorDocQuery.append(" VALUES (?, ?)");
            try {
                PreparedStatement preparedStatement;
                preparedStatement = DatabaseManagement.getConnection().prepareStatement(authorDocQuery.toString());
                preparedStatement.setString(1, doc.getId());
                preparedStatement.setString(2, author.getId());
                preparedStatement.executeUpdate();

                if (!AuthorDAO.checkAuthorExist(author.getName())) {
                    AuthorDAO.addAuthor(author);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Xóa tài liệu.
    public static void removeDocument(String docId) {
        StringBuilder docQuery = new StringBuilder();
        docQuery.append("DELETE FROM documents ");
        docQuery.append("WHERE document_id = ?");

        StringBuilder authorQuery = new StringBuilder();
        authorQuery.append("DELETE FROM document_author ");
        authorQuery.append("WHERE document_id = ?");

        StringBuilder categoryQuery = new StringBuilder();
        categoryQuery.append("DELETE FROM document_category ");
        categoryQuery.append("WHERE document_id = ?");

        StringBuilder receiptsQuery = new StringBuilder();
        receiptsQuery.append("DELETE FROM receipts ");
        receiptsQuery.append("WHERE document_id = ?");

        StringBuilder ratingsQuery = new StringBuilder();
        ratingsQuery.append("DELETE FROM ratings ");
        ratingsQuery.append("WHERE document_id = ?");

        try {
            // Xóa tài liệu từ bảng documents
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getConnection().prepareStatement(docQuery.toString());
            preparedStatement.setString(1, docId);
            preparedStatement.executeUpdate();

            // Xóa bản ghi liên quan trong bảng document_author
            preparedStatement = DatabaseManagement.getConnection().prepareStatement(authorQuery.toString());
            preparedStatement.setString(1, docId);
            preparedStatement.executeUpdate();

            // Xóa bản ghi liên quan trong bảng document_category
            preparedStatement = DatabaseManagement.getConnection().prepareStatement(categoryQuery.toString());
            preparedStatement.setString(1, docId);
            preparedStatement.executeUpdate();

            // Xóa bản ghi liên quan trong bảng receipts
            preparedStatement = DatabaseManagement.getConnection().prepareStatement(receiptsQuery.toString());
            preparedStatement.setString(1, docId);
            preparedStatement.executeUpdate();

            // Xóa bản ghi liên quan trong bảng ratings

            preparedStatement = DatabaseManagement.getConnection().prepareStatement(ratingsQuery.toString());
            preparedStatement.setString(1, docId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error when removing document with id: " + docId, e);
        }
    }


    // Kiểm tra xem tài liệu có tồn tại dựa trên tiêu đề
    public static boolean checkDocumentExist(String title) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM documents WHERE title = ?");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, title);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // Nếu có kết quả thì tài liệu tồn tại
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Thêm tài liệu.
    public static void updateDocument(Document document) {
        //Xóa doc cũ tương ứng với id sau đó thêm doc mới
        removeDocument(document.getId());
        addDocument(document);
    }
}