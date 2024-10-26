package base;

import app.base.Document;
import app.base.Member;
import app.base.Rating;
import app.base.Receipt;
import app.database.DatabaseManagement;
import app.database.DocumentDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class MemberTest {
    private Member member;
    private Document document;

    @BeforeEach
    void setUp() {
        DatabaseManagement.setConnection();

        member = new Member(
                "",
                "user1",
                "pass1",
                "First",
                "Last",
                "01-01-1992",
                "email@example.com",
                "123456789");
        document = DocumentDAO.getDocFromId("SWCPZGEACAAJ");
    }

    @Test
    void testBorrowDocument() {
        int initialRemaining = document.getRemaining(); // Số lượng document còn lại trước khi mượn.
        member.borrowDocument(document);
        Receipt receipt = member.getReceipts().getFirst();
        assertEquals(initialRemaining - 1, document.getRemaining());
        assertEquals("not returned", receipt.getStatus());
        assertEquals(LocalDate.now().toString(), receipt.getBorrowingDate());
        assertEquals(LocalDate.now().plusWeeks(2).toString(), receipt.getReturnDate());
    }

    @Test
    void testReturnDocument() {
        int initialRemaining = document.getRemaining();
        member.borrowDocument(document);
        Receipt receipt = member.getReceipts().getFirst();
        member.returnDocument(receipt);
        Document updatedDocument = DocumentDAO.getDocFromId(document.getId());
        assert updatedDocument != null;
        assertEquals(initialRemaining, updatedDocument.getRemaining());
        assertEquals("returned", receipt.getStatus());
    }

    @Test
    void testRateDocument() {
        int initialRatingCount = document.getRatingCount();
        double initialAverageScore = document.getAverageScore();

        Rating rating = new Rating(
                "rating1",
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
