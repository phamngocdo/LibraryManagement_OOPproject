package base;

import app.base.*;
import app.dao.DocumentDAO;
import app.dao.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//All test are correct
public class MemberTest {
    private Member member;
    private Document document;

    @BeforeEach
    void setUp() {
        member = (Member) UserDAO.getUserFromLogin("member1", "member1");
        document = DocumentDAO.getDocFromId("6BaJDwAAQBAJ");
    }

    @Test
    void testBorrowDocument() {
        int initialRemaining = document.getRemaining();
        member.borrowDocument(document);
        Receipt receipt = member.getReceipts().getFirst();
        assertEquals(initialRemaining - 1, document.getRemaining());
        assertEquals("not returned", receipt.getStatus());
    }

    @Test
    void testRateDocument() {
        int initialRatingCount = document.getRatingCount();
        double initialAverageScore = document.getAverageScore();

        Rating rating = new Rating(
                "",
                member.getId(),
                document.getId(),
                5,
                "Great Document!");

        member.rateDocument(rating, document);

        Document updatedDocument = DocumentDAO.getDocFromId(document.getId());

        assert updatedDocument != null;
        assertEquals(initialRatingCount + 1, updatedDocument.getRatingCount());

        double expectedAverageScore = (initialRatingCount * initialAverageScore + rating.getRatingScore())
                / (initialRatingCount + 1);

        assertEquals(expectedAverageScore, updatedDocument.getAverageScore(), 0.001);
    }

}
