package app.trie;

import app.dao.DocumentDAO;

public class DocumentTitleTrie extends Trie{

    public DocumentTitleTrie() {
        DocumentDAO.getAllDoc().forEach(doc -> insertWord(doc.getId(), doc.getTitle()));
    }
}
