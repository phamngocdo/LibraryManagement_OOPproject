package app.trie;

import java.util.HashMap;
import javafx.util.Pair;

class TrieNode {
    private final HashMap<Character, TrieNode> children;
    private boolean isWord;
    private Pair<String, String> word;

    public TrieNode(){
        this.children = new HashMap<>();
        this.isWord = false;
        this.word = null;
    }


    public void addChild(Character character){
        children.computeIfAbsent(character, k -> new TrieNode());
    }

    public TrieNode getChild(Character character){
        return children.get(character);
    }

    public void setEndOfWord(boolean isWord){
        this.isWord = isWord;
    }

    public boolean isEndOfWord(){
        return isWord;
    }

    public void setWord(String wordId, String word){
        if(wordId == null || word == null){
            this.word = null;
        }
        else{
            this.word = new Pair<>(wordId, word);
        }
    }

    public Pair<String, String> getWord(){
        return word;
    }

    public HashMap<Character, TrieNode> getAllChildren(){
        return children;
    }
}