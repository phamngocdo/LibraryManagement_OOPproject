package database;

import app.base.Rating;
import app.dao.RatingDAO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

//All Test are correct
public class RatingDAOTest {
    @Test
    public void testGetRatingFromId_Valid() {
        Rating rating = RatingDAO.getRatingFromId("F7P3R9K2YJLM");
        assertNotNull(rating);
        //kiểm tra user_id có đúng k
        assertEquals("OJIK98JHNTMT", rating.getMemberId());
    }

    @Test
    public void testGetRatingFromId_Invalid() {
        Rating rating = RatingDAO.getRatingFromId("invalid-id");
        assertNull(rating);
    }

    @Test
    public void testGetAllRatingFromDocId_Valid() {
        ArrayList<Rating> ratings = RatingDAO.getAllRatingFromDocId("HemnRxzdiFQC");
        assertNotNull(ratings);
        assertFalse(ratings.isEmpty());
    }

    @Test
    public void testAddRating() {
        Rating newRating = new Rating("", "OJIK98JHNTMT", "docid", 5, "Great!");
        RatingDAO.addRating(newRating);
        Rating rating = RatingDAO.getRatingFromId(newRating.getId());
        assertNotNull(rating);
        assertEquals("Great!", rating.getComment());
    }

    @Test
    public void testRemoveRating() {
        Rating ratingToRemove = new Rating("", "user", "doc", 4, "Good");
        RatingDAO.addRating(ratingToRemove);
        RatingDAO.removeRating(ratingToRemove.getId());
        Rating removedRating = RatingDAO.getRatingFromId(ratingToRemove.getId());
        assertNull(removedRating);
    }
}
