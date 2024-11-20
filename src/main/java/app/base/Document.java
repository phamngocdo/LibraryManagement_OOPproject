package app.base;

import app.dao.AuthorDAO;
import app.dao.CategoryDAO;
import app.dao.DocumentDAO;
import app.dao.RatingDAO;
import javafx.scene.image.Image;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class Document {
    private String id;
    private String title;
    private String isbn;
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

    public Document(String id, String title, String isbn, int quantity, int remaining,
                    int ratingCount, double averageScore, int pageCount, String description,
                    String publisher, String publishedDate, String imageUrl,
                    ArrayList<Author> authors, ArrayList<Category> categories, ArrayList<Rating> ratings) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.quantity = quantity;
        this.remaining = remaining;
        this.ratingCount = ratingCount;
        this.averageScore = averageScore;
        this.imageUrl = imageUrl;
        this.pageCount = pageCount;
        this.description = description;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.authors = Objects.requireNonNullElseGet(authors, () -> AuthorDAO.getAllAuthorFromDocId(id));
        this.categories = Objects.requireNonNullElseGet(categories, () -> CategoryDAO.getAllCategoryFromDocId(id));
        this.ratings = Objects.requireNonNullElseGet(ratings, () -> RatingDAO.getAllRatingFromDocId(id));
    }

    public Document(String id) {
        Document doc = DocumentDAO.getDocFromId(id);
        if (doc != null) {
            this.id = id;
            title = doc.getTitle();
            isbn = doc.getIsbn();
            quantity = doc.getQuantity();
            remaining = doc.getRemaining();
            ratingCount = doc.getRatingCount();
            averageScore = doc.getAverageScore();
            imageUrl = doc.getImageUrl();
            pageCount = doc.getPageCount();
            description = doc.getDescription();
            publisher = doc.getPublisher();
            publishedDate = doc.getPublishedDate();
            authors = AuthorDAO.getAllAuthorFromDocId(id);
            categories = CategoryDAO.getAllCategoryFromDocId(id);
            ratings = RatingDAO.getAllRatingFromDocId(id);
        }
    }

    public Document(ResultSet resultSet) throws SQLException {
        if (resultSet != null) {
            id = resultSet.getString("document_id");
            title = resultSet.getString("title");
            isbn = resultSet.getString("isbn");
            quantity = resultSet.getInt("quantity");
            remaining = resultSet.getInt("remaining");
            ratingCount = resultSet.getInt("ratings_count");
            averageScore = resultSet.getDouble("average_score");
            imageUrl = resultSet.getString("image_url");
            pageCount = resultSet.getInt("page_count");
            description = resultSet.getString("description");
            publisher = resultSet.getString("publisher");
            publishedDate = resultSet.getString("published_date");
            authors = AuthorDAO.getAllAuthorFromDocId(id);
            categories = CategoryDAO.getAllCategoryFromDocId(id);
            ratings = RatingDAO.getAllRatingFromDocId(id);
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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
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

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
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

    public void setAuthors(ArrayList<Author> authors) {
        this.authors = authors;
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
        if (imageUrl == null) {
            imageUrl = String.valueOf(getClass().getResource("/graphic/images/no-image.png"));
        }
        return new Image(imageUrl);
    }
}
