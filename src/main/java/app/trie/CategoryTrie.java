package app.trie;

import app.dao.CategoryDAO;

public class CategoryTrie extends Trie {

    public CategoryTrie() {
        CategoryDAO.getAllCategories().forEach(category -> insertWord(category.getId(), category.getCategory()));
    }
}
