package app.dao;
import app.base.Author;
import app.base.Category;
import app.base.Document;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DocumentDAO {

    public static Document getDocFromId(String docId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM documents ");
        query.append(" WHERE document_id = ?");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, docId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Document(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static ArrayList<Document> getAllDoc() {
        ArrayList<Document> documents = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM documents");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(query.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                documents.add(new Document(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return documents;
    }

    public static ArrayList<Document> getAllDocumentFromAuthor(String authorId) {
        ArrayList<Document> documents = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT d.* FROM documents AS d ");
        query.append("JOIN document_author AS da ON d.document_id = da.document_id ");
        query.append("JOIN authors AS a ON da.author_id = a.author_id ");
        query.append("WHERE da.author_id = ?");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, authorId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                documents.add(new Document(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return documents;
    }

    public static ArrayList<Document> getAllDocumentFromCategory(String categoryId) {
        ArrayList<Document> documents = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT d.* FROM documents AS d ");
        query.append("JOIN document_category AS dc ON d.document_id = dc.document_id ");
        query.append("JOIN categories AS c ON dc.category_id = c.category_id ");
        query.append("WHERE c.category_id = ?");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, categoryId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                documents.add(new Document(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return documents;
    }

    public static ArrayList<Document> getBestRatingDocuments(int number) {
        ArrayList<Document> documents = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM documents ORDER BY average_score DESC LIMIT ?");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(query.toString());
            preparedStatement.setInt(1, number);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                documents.add(new Document(resultSet));
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
        return documents;
    }

    public static void addDocument(Document doc) {
        if (doc.getId() == null || doc.getId().isEmpty()) {
            doc.setId(IdGenerator.createRandomIdInTable("documents", "document_id"));
        }
        StringBuilder docQuery = new StringBuilder();
        docQuery.append("INSERT INTO documents ");
        docQuery.append("(document_id, title, isbn, quantity, remaining, ratings_count, average_score, " +
                "description, page_count, publisher, published_date, image_url) ");
        docQuery.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(docQuery.toString());
            preparedStatement.setString(1, doc.getId());
            preparedStatement.setString(2, doc.getTitle());
            preparedStatement.setString(3, doc.getIsbn());
            preparedStatement.setInt(4, doc.getQuantity());
            preparedStatement.setInt(5, doc.getRemaining());
            preparedStatement.setInt(6, doc.getRatingCount());
            preparedStatement.setDouble(7, doc.getAverageScore());
            preparedStatement.setString(8, doc.getDescription());
            preparedStatement.setInt(9, doc.getPageCount());
            preparedStatement.setString(10, doc.getPublisher());
            preparedStatement.setString(11, doc.getPublishedDate());
            preparedStatement.setString(12, doc.getImageUrl());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (Category category : doc.getCategories()) {
            String id = CategoryDAO.getOrAddCategory(category);
            StringBuilder categoryDocQuery = new StringBuilder();
            categoryDocQuery.append("INSERT INTO document_category (document_id, category_id)");
            categoryDocQuery.append(" VALUES (?, ?)");
            try {
                PreparedStatement preparedStatement;
                preparedStatement = DatabaseManagement.getInstance().getConnection()
                        .prepareStatement(categoryDocQuery.toString());
                preparedStatement.setString(1, doc.getId());
                preparedStatement.setString(2, id);
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        for (Author author : doc.getAuthors()) {
            String id = AuthorDAO.getOrAddAuthor(author);
            StringBuilder authorDocQuery = new StringBuilder();
            authorDocQuery.append("INSERT INTO document_author (document_id, author_id)");
            authorDocQuery.append(" VALUES (?, ?)");
            try {
                PreparedStatement preparedStatement;
                preparedStatement = DatabaseManagement.getInstance().getConnection()
                        .prepareStatement(authorDocQuery.toString());
                preparedStatement.setString(1, doc.getId());
                preparedStatement.setString(2, id);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

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
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getInstance().getConnection()
                    .prepareStatement(docQuery.toString());
            preparedStatement.setString(1, docId);
            preparedStatement.executeUpdate();

            preparedStatement = DatabaseManagement.getInstance().getConnection()
                    .prepareStatement(authorQuery.toString());
            preparedStatement.setString(1, docId);
            preparedStatement.executeUpdate();

            preparedStatement = DatabaseManagement.getInstance().getConnection()
                    .prepareStatement(categoryQuery.toString());
            preparedStatement.setString(1, docId);
            preparedStatement.executeUpdate();

            preparedStatement = DatabaseManagement.getInstance().getConnection().
                    prepareStatement(receiptsQuery.toString());
            preparedStatement.setString(1, docId);
            preparedStatement.executeUpdate();

            preparedStatement = DatabaseManagement.getInstance().getConnection()
                    .prepareStatement(ratingsQuery.toString());
            preparedStatement.setString(1, docId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean checkDocumentExist(String id) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM documents WHERE document_id = ?");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateDocument(Document document) {
        StringBuilder query = new StringBuilder();
        query.append("UPDATE documents ");
        query.append("SET isbn = ?, title = ?, publisher = ?, published_date = ?, description = ?, page_count = ?,");
        query.append(" average_score = ?, quantity = ?, remaining = ?, ratings_count = ?, image_url = ?");
        query.append("WHERE document_id = ?");
        try {
            PreparedStatement preparedStatement;
            preparedStatement = DatabaseManagement.getInstance().getConnection().prepareStatement(query.toString());
            preparedStatement.setString(1, document.getIsbn());
            preparedStatement.setString(2, document.getTitle());
            preparedStatement.setString(3, document.getPublisher());
            preparedStatement.setString(4, document.getPublishedDate());
            preparedStatement.setString(5, document.getDescription());
            preparedStatement.setInt(6, document.getPageCount());
            preparedStatement.setDouble(7, document.getAverageScore());
            preparedStatement.setInt(8, document.getQuantity());
            preparedStatement.setInt(9, document.getRemaining());
            preparedStatement.setInt(10, document.getRatingCount());
            preparedStatement.setString(11, document.getImageUrl());
            preparedStatement.setString(12, document.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}