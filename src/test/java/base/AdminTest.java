package base;

import app.base.Admin;
import app.base.Document;
import app.base.Member;
import app.base.Receipt;
import app.dao.DatabaseManagement;
import app.dao.DocumentDAO;
import app.dao.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

//All test are correct
public class AdminTest {
    @BeforeEach
    public void setUp() {
        DatabaseManagement.setConnection();
    }

    @Test
    public void testConfirmReturnDocument() {
        Admin admin = (Admin) UserDAO.getUserFromLogin("admin", "admin");
        Member member = (Member) UserDAO.getUserFromLogin("member1", "member1");
        Document document = DocumentDAO.getDocFromId("6BaJDwAAQBAJ");
        assert document != null;
        int initialRemaining = document.getRemaining();
        assert member != null;
        member.borrowDocument(document);
        Receipt receipt = member.getReceipts().getFirst();
        assert admin != null;
        admin.confirmReturnDocument(receipt);
        Document updatedDocument = DocumentDAO.getDocFromId(document.getId());
        assert updatedDocument != null;
        assertEquals(initialRemaining + 1, updatedDocument.getRemaining());
        assertEquals("returned", receipt.getStatus());
    }
}
