package base;

import app.base.*;
import app.database.DatabaseManagement;
import app.database.DocumentDAO;
import app.database.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

//All test are correct
public class MemberTest {
    private Member member;
    private Document document;

    @BeforeEach
    void setUp() {
        DatabaseManagement.setConnection();

        member = (Member) UserDAO.getUserFromLogin("member1", "member1");
        document = DocumentDAO.getDocFromId("6BaJDwAAQBAJ");
    }

    @Test
    void testBorrowDocument() {
        int initialRemaining = document.getRemaining(); // Số lượng document còn lại trước khi mượn.
        member.borrowDocument(document);
        Receipt receipt = member.getReceipts().getFirst();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.now();
        assertEquals(initialRemaining - 1, document.getRemaining());
        assertEquals("not returned", receipt.getStatus());
        assertEquals(date.format(formatter), receipt.getBorrowingDate());
        assertEquals(date.plusWeeks(2).format(formatter), receipt.getReturnDate());
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

        member.rateDocument(rating);

        Document updatedDocument = DocumentDAO.getDocFromId(document.getId());

        assert updatedDocument != null;
        assertEquals(initialRatingCount + 1, updatedDocument.getRatingCount());

        double expectedAverageScore = (initialRatingCount * initialAverageScore + rating.getRatingScore())
                / (initialRatingCount + 1);

        assertEquals(expectedAverageScore, updatedDocument.getAverageScore(), 0.001);
    }

}
