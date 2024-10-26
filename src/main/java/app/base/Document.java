package app.base;

import app.database.AuthorDAO;
import app.database.CategoryDAO;
import app.database.DocumentDAO;
import app.database.RatingDAO;
import app.service.GoogleBookAPI;
import javafx.scene.image.Image;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Document {
    private String id;
    private String title;
    private ArrayList<Author> authors;
    private ArrayList<Category> categories;
    private ArrayList<Rating> ratings;
    private int quantity;
    private int remaining;
    private int ratingCount;
    private double averageScore;
    private int pageCount;
    private String description;
    private String publisher;
    private String publishedDate;
    private String imageUrl;

    public Document(String id, String title, int quantity, int remaining,
                    int ratingCount, double averageScore, int pageCount, String description,
                    String publisher, String publishedDate, String imageUrl ) {
        this.id = id;
        this.title = title;
        this.quantity = quantity;
        this.remaining = remaining;
        this.ratingCount = ratingCount;
        this.averageScore = averageScore;
        this.imageUrl = imageUrl;
        this.pageCount = pageCount;
        this.description = description;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        authors = AuthorDAO.getAllAuthorFromDocId(id);
        categories = CategoryDAO.getAllCategoryFromDocId(id);
        ratings = RatingDAO.getAllRatingFromDocId(id);
    }

    public Document(String id) {
        Document doc = DocumentDAO.getDocFromId(id);
        if (doc != null) {
            this.id = id;
            title = doc.title;
            quantity = doc.quantity;
            remaining = doc.remaining;
            ratingCount = doc.ratingCount;
            averageScore = doc.averageScore;
            imageUrl = doc.imageUrl;
            authors = AuthorDAO.getAllAuthorFromDocId(id);
            categories = CategoryDAO.getAllCategoryFromDocId(id);
            ratings = RatingDAO.getAllRatingFromDocId(id);
        }
    }

    public Document(ResultSet resultSet) throws SQLException {
        if (resultSet != null) {
            id = resultSet.getString("document_id");
            title = resultSet.getString("title");
            quantity = resultSet.getInt("quantity");
            remaining = resultSet.getInt("remaining");
            ratingCount = resultSet.getInt("ratings_count");
            averageScore = resultSet.getDouble("average_score");
            imageUrl = resultSet.getString("image_url");
            pageCount = resultSet.getInt("page_count");
            description = resultSet.getString("description");
            publisher = resultSet.getString("publisher");
            publishedDate = resultSet.getString("published_date");
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public ArrayList<Rating> getRatings() {
        return ratings;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public String getCategoriesToString() {
        //Trả về dưới dạng chuỗi cách nhau bởi dấu phẩy như: Kinh dị, Hài hước, Kỹ thuật
        StringBuilder categoriesString = new StringBuilder();
        for (int i = 0; i < categories.size(); i++) {
            categoriesString.append(categories.get(i).getCategory());
            if (i < categories.size() - 1) {
                categoriesString.append(", ");
            }
        }
        return categoriesString.toString();
    }

    public ArrayList<Author> getAuthors() {
        return authors;
    }

    public String getAuthorsToString() {
        StringBuilder authorsString = new StringBuilder();
        for (int i = 0; i < authors.size(); i++) {
            authorsString.append(authors.get(i).getName());
            if (i < authors.size() - 1) {
                authorsString.append(", ");
            }
        }
        return authorsString.toString();
    }

    public Image loadImage() {
        return new Image(imageUrl);
    }

    public HashMap<String, String> getFullInfoFromAPI() throws Exception {
        return GoogleBookAPI.getDocFromId(id);
    }
}
