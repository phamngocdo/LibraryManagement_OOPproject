package base;

import app.base.Admin;
import app.base.Document;
import app.base.Member;
import app.base.Receipt;
import app.dao.DocumentDAO;
import app.dao.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

//All test are correct
public class AdminTest {
    Document document;
    Member member;

    @BeforeEach
    void setUp() {
        member = (Member) UserDAO.getUserFromLogin("member1", "member1");
        document = DocumentDAO.getDocFromId("6BaJDwAAQBAJ");
        assert document != null;
        assert member != null;
        member.borrowDocument(document);
    }

    @Test
    public void testConfirmReturnDocument() {
        Admin admin = (Admin) UserDAO.getUserFromLogin("admin", "admin");
        int initialRemaining = document.getRemaining();
        Receipt receipt = member.getReceipts().getFirst();
        assert admin != null;
        admin.confirmReturnDocument(receipt, true);
        Document updatedDocument = DocumentDAO.getDocFromId(document.getId());
        assert updatedDocument != null;
        assertEquals(initialRemaining, updatedDocument.getRemaining());
        assertEquals("returned", receipt.getStatus());
    }
}
