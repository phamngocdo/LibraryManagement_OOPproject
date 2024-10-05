package app.database;

import app.base.Rating;
import java.sql.*;
import java.util.ArrayList;

public class RatingDAO {
    public static final String MAIN_TABLE = "ratings";

    public static Rating getRatingFromId(String ratingId) {
        //

    }

    public static ArrayList<Rating> getAllRatingFromDocId(String docId) {
        //

    }

    public static ArrayList<Rating> getAllRating() {
        //

    }

    public static void addRating(Rating rating) {
        rating.setId(DatabaseManagement.createRandomIdInTable(MAIN_TABLE, "rating_id"));
        //
    }

    public static void removeRating(String ratingId) {
        //
    }
}
