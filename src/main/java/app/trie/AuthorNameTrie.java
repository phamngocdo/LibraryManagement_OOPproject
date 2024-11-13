package app.trie;

import app.dao.AuthorDAO;

public class AuthorNameTrie extends Trie {

    public AuthorNameTrie() {
        AuthorDAO.getAllAuthors().forEach(author -> insertWord(author.getId(), author.getName()));
    }
}
