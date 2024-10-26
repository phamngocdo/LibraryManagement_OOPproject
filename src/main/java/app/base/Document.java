package app.base;

import app.database.AuthorDAO;
import app.database.CategoryDAO;
import app.database.RatingDAO;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class Document {
    private String id;
    private String title;
    private String isbn;
    private final ArrayList<Author> authors;
    private final ArrayList<Category> categories;
    private final ArrayList<Rating> ratings;
    private int quantity;
    private int remaining;
    private int ratingCount;
    private double averageScore;
    private int pageCount;
    private String description;
    private String publisher;
    private String publisherDate;
    private String imageUrl;

    public Document(String id, String title, String isbn, int quantity, int remaining,
                    int ratingCount, double averageScore, int pageCount, String description,
                    String publisher, String publisherDate, String imageUrl) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.quantity = quantity;
        this.remaining = remaining;
        this.ratingCount = ratingCount;
        this.averageScore = averageScore;
        this.pageCount = pageCount;
        this.description = description;
        this.publisher = publisher;
        this.publisherDate = publisherDate;
        this.imageUrl = imageUrl;
        authors = AuthorDAO.getAllAuthorFromDocId(id);
        categories = CategoryDAO.getAllCategoryFromDocId(id);
        ratings = RatingDAO.getAllRatingFromDocId(id);
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

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublisherDate() {
        return publisherDate;
    }

    public void setPublisherDate(String publisherDate) {
        this.publisherDate = publisherDate;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ArrayList<Rating> getRatings() {
        return ratings;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public String getCategoriesToString() {
        //Trả về dưới dạng chuỗi cách nhau bởi dấu phẩy như: Kinh dị, Hài hước, Kỹ thuật
        return "";
    }

    public ArrayList<Author> getAuthors() {
        return authors;
    }

    public String getAuthorsToString() {
        //Tương tự
        return "";
    }

    public ImageView loadImage() {
        //Hàm này để sau
        return new ImageView();
    }

    public HashMap<String, String> seeFullInfoFromApi() {
        //Hàm này để sau
        return new HashMap<>();
    }
}
