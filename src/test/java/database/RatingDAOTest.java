package database;

import app.base.Rating;
import app.database.DatabaseManagement;
import app.database.RatingDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class RatingDAOTest {

    @BeforeEach
    public void setUp() {
        DatabaseManagement.setConnection();
    }

    @Test
    public void testGetRatingFromId_Valid() {
        Rating rating = RatingDAO.getRatingFromId("rating123");
        assertNotNull(rating);
        assertEquals("rating123", rating.getId());
    }

    @Test
    public void testGetRatingFromId_Invalid() {
        Rating rating = RatingDAO.getRatingFromId("invalidId");
        assertNull(rating);
    }

    @Test
    public void testGetRatingFromId_Null() {
        Rating rating = RatingDAO.getRatingFromId(null);
        assertNull(rating);
    }

    @Test
    public void testGetAllRatingFromDocId_Valid() {
        ArrayList<Rating> ratings = RatingDAO.getAllRatingFromDocId("doc123");
        assertNotNull(ratings);
        assertTrue(ratings.size() > 0);
    }

    @Test
    public void testGetAllRatingFromDocId_Invalid() {
        ArrayList<Rating> ratings = RatingDAO.getAllRatingFromDocId("invalidDocId");
        assertNotNull(ratings);
        assertTrue(ratings.isEmpty());
    }

    @Test
    public void testGetAllRatingFromDocId_Null() {
        ArrayList<Rating> ratings = RatingDAO.getAllRatingFromDocId(null);
        assertNotNull(ratings);
        assertTrue(ratings.isEmpty());
    }

    @Test
    public void testGetAllRating() {
        ArrayList<Rating> ratings = RatingDAO.getAllRating();
        assertNotNull(ratings);
        assertTrue(ratings.size() > 0);
    }

    @Test
    public void testAddRating() {
        Rating newRating = new Rating("newRatingId", "userId123", "docId123", 5, "Great!");
        RatingDAO.addRating(newRating);
        Rating rating = RatingDAO.getRatingFromId("newRatingId");
        assertNotNull(rating);
        assertEquals("Great!", rating.getComment());
    }

    @Test
    public void testRemoveRating() {
        Rating ratingToRemove = new Rating("removeRatingId", "userId456", "docId456", 4, "Good");
        RatingDAO.addRating(ratingToRemove);
        RatingDAO.removeRating("removeRatingId");
        Rating removedRating = RatingDAO.getRatingFromId("removeRatingId");
        assertNull(removedRating); // Rating nên được xóa
    }

    @Test
    public void testRemoveRating_InvalidId() {
        assertThrows(SQLException.class, () -> {
            RatingDAO.removeRating("invalidRatingId");
        });
    }
}
