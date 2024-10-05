package app.base;

public class Category {
    private String id;
    private String category;

    public Category(String id, String category) {
        this.id = id;
        this.category = category;
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
