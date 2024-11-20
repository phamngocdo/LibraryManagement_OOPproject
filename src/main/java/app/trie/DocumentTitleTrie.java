package app.trie;

import app.dao.DatabaseManagement;
import app.dao.DocumentDAO;
import javafx.util.Pair;

public class DocumentTitleTrie extends Trie{

    public DocumentTitleTrie() {
        DocumentDAO.getAllDoc().forEach(doc -> insertWord(doc.getId(), doc.getTitle()));
    }

    public static void main(String[] args) {
        DatabaseManagement.setConnection();
        Trie trie = new DocumentTitleTrie();
        for (Pair<String, String> e : trie.getAllWordStartWith("linux")) {
            System.out.println(e.getKey() + " " + e.getValue());
        }
    }
}
