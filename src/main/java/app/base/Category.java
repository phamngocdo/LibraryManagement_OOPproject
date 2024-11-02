package app.base;

import app.database.DocumentDAO;

import java.util.ArrayList;

public class Category {
    private String id;
    private String category;

    public Category(String id, String category) {
        this.id = id;
        this.category = category;
    }

    public ArrayList<Document> getAllDoc() {
        return DocumentDAO.getAllDocumentFromCategory(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
