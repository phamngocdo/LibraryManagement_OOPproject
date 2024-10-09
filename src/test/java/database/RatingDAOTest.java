package database;

import app.base.Rating;
import app.database.DatabaseManagement;
import app.database.RatingDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class RatingDAOTest {

    @BeforeEach
    public void setUp() {
        DatabaseManagement.setConnection();
    }

    @Test
    public void testGetRatingFromId_Valid() {
        Rating rating = RatingDAO.getRatingFromId("F7P3R9K2YJLM");
        assertNotNull(rating);
        //kiểm tra user_id có đúng k
        assertEquals("OJIK98JHNTMT", rating.getUserId());
    }

    @Test
    public void testGetRatingFromId_Invalid() {
        Rating rating = RatingDAO.getRatingFromId("invalid-id");
        assertNull(rating);
    }

    @Test
    public void testGetAllRatingFromDocId_Valid() {
        ArrayList<Rating> ratings = RatingDAO.getAllRatingFromDocId("HEMNRXZDIFQC");
        assertNotNull(ratings);
        assertFalse(ratings.isEmpty());
    }

    @Test
    public void testGetAllRating() {
        ArrayList<Rating> ratings = RatingDAO.getAllRating();
        assertNotNull(ratings);
        assertFalse(ratings.isEmpty());
    }

    @Test
    public void testAddRating() {
        Rating newRating = new Rating("newratingid", "userid", "docid", 5, "Great!");
        RatingDAO.addRating(newRating);
        Rating rating = RatingDAO.getRatingFromId("newratingid");
        assertNotNull(rating);
        assertEquals("Great!", rating.getComment());
    }

    @Test
    public void testRemoveRating() {
        Rating ratingToRemove = new Rating("newratingid2", "user", "doc", 4, "Good");
        RatingDAO.addRating(ratingToRemove);
        RatingDAO.removeRating("newratingid2");
        Rating removedRating = RatingDAO.getRatingFromId("newratingid2");
        assertNull(removedRating);
    }
}
