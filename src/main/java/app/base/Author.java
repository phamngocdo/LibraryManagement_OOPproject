package app.base;

import app.dao.DocumentDAO;

import java.util.ArrayList;

public class Author {
    private String id;
    private String name;

    public Author(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public ArrayList<Document> getAllDocument() {
        return DocumentDAO.getAllDocumentFromAuthor(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
