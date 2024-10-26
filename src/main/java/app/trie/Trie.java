package app.trie;

import java.util.ArrayList;
import javafx.util.Pair;

public class Trie {
    private TrieNode root = new TrieNode();

    public void insertWord(String wordId, String word) {
        if (root == null) {
            root = new TrieNode();
        }
        TrieNode current = root;
        String lowerWord = word.toLowerCase();

        for (char ch : lowerWord.toCharArray()) {
            if (current.getChild(ch) == null) {
                current.addChild(ch);
            }
            current = current.getChild(ch);
        }

        if (!current.isEndOfWord()) {
            current.setEndOfWord(true);
            current.setWord(wordId, word);
        }
    }

    private TrieNode getEndNode(String word) {
        if (root == null) {
            return null;
        }
        TrieNode current = root;
        for (char ch : word.toCharArray()) {
            current = current.getChild(ch);
            if (current == null) {
                return null;
            }
        }
        return current;
    }

    public ArrayList<Pair<String, String>> getAllWordStartWith(String prefix) {
        ArrayList<Pair<String, String>> list = new ArrayList<>();
        prefix = prefix.toLowerCase();
        TrieNode current = getEndNode(prefix);
        if (current == null) {
            return list;
        }
        collectAllWord(current, list);
        return list;
    }

    private void collectAllWord(TrieNode current, ArrayList<Pair<String, String>> list) {
        if (current == null) {
            return;
        }
        if (current.isEndOfWord()) {
            list.add(current.getWord());
        }
        for (char ch : current.getAllChildren().keySet()) {
            collectAllWord(current.getChild(ch), list);
        }
    }

    public Pair<String, String> getWord(String word){
        TrieNode current = getEndNode(word);
        if (current == null || !current.isEndOfWord()){
            return null;
        }
        return current.getWord();
    }
}