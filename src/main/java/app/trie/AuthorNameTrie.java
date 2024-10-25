package app.trie;

import app.database.AuthorDAO;

public class AuthorNameTrie extends Trie {

    public AuthorNameTrie() {
        AuthorDAO.getAllAuthors().forEach(author -> insertWord(author.getId(), author.getName()));
    }
}
