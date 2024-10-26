package app.trie;

import app.database.CategoryDAO;

public class CategoryTrie extends Trie {

    public CategoryTrie() {
        CategoryDAO.getAllCategories().forEach(category -> insertWord(category.getId(), category.getCategory()));
    }
}
