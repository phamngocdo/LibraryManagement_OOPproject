package app.database;

import app.base.Author;

import java.util.ArrayList;
import java.sql.*;

public class AuthorDAO {
    public static String MAIN_TABLE = "authors";

    public static ArrayList<Author> getAllAuthorFromDocId(String docId) {
        return new ArrayList<>();
    }

    public static void addAuthor(Author author) {
        author.setId(DatabaseManagement.createRandomIdInTable(MAIN_TABLE, "author_id"));
        //
    }
}
