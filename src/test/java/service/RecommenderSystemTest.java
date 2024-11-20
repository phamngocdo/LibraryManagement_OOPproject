package service;

import app.base.Document;
import app.base.Member;
import app.base.Receipt;
import app.dao.DatabaseManagement;
import app.dao.ReceiptDAO;
import app.dao.UserDAO;
import app.service.RecommenderSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class RecommenderSystemTest {

    private Member member;

    @BeforeEach
    public void setUp() {
        // Thiết lập dữ liệu
        DatabaseManagement.setConnection(); // Kết nối database
        member = new Member(
                "OJIK98JHNTMT",
                "test_member",
                "password",
                "John Doe",
                "johndoe@gmail.com",
                "01-01-1990",
                "123456789",
                "Address 123"
        );
        UserDAO.addMember(member); // Thêm thành viên vào database

        // Xóa tất cả các biên lai cũ của member
        ArrayList<Receipt> receipts = ReceiptDAO.getAllReceiptFromMemberId(member.getId());
        for (Receipt receipt : receipts) {
            ReceiptDAO.removeReceipt(receipt.getId());
        }
    }

    @Test
    public void testGetRecommendDocFromMember_NoReceipts() {
        // Kiểm tra khi member chưa có bất kỳ biên lai nào
        ArrayList<Receipt> receipts = ReceiptDAO.getAllReceiptFromMemberId(member.getId());
        assertTrue(receipts.isEmpty()); // Chưa mượn sách nào

        // Gọi phương thức cần kiểm thử
        ArrayList<Document> recommendedDocs = RecommenderSystem.getRecommendDocFromMember(member);

        // Đảm bảo hệ thống trả về 5 sách
        assertNotNull(recommendedDocs);
        assertEquals(5, recommendedDocs.size());
    }

    @Test
    public void testGetRecommendDocFromMember_WithReceipts() {
        // Tạo một biên lai giả
        Receipt receipt = new Receipt(
                "R001",
                member.getId(),
                "DOC123",
                "2024-11-01",
                "2024-11-15",
                ""
        );
        ReceiptDAO.addReceipt(receipt); // Thêm biên lai vào database

        // Kiểm tra biên lai đã được thêm
        ArrayList<Receipt> receipts = ReceiptDAO.getAllReceiptFromMemberId(member.getId());
        assertFalse(receipts.isEmpty());

        // Gọi phương thức cần kiểm thử
        ArrayList<Document> recommendedDocs = RecommenderSystem.getRecommendDocFromMember(member);

        // Kiểm tra số lượng tài liệu được gợi ý
        assertNotNull(recommendedDocs);
        assertTrue(recommendedDocs.size() > 0); // Có ít nhất 1 sách được gợi ý
    }

    @Test
    public void testGetRecommendDocFromMember_LessThanFiveDocs() {
        // Giả định chỉ có 2 tài liệu trong hệ thống có thể gợi ý
        Receipt receipt = new Receipt(
                "R002",
                member.getId(),
                "DOC456",
                "2024-11-01",
                "2024-11-15",
                ""
        );
        ReceiptDAO.addReceipt(receipt);

        // Gọi phương thức cần kiểm thử
        ArrayList<Document> recommendedDocs = RecommenderSystem.getRecommendDocFromMember(member);

        // Kiểm tra nếu danh sách gợi ý < 5 thì thêm từ tài liệu xếp hạng cao
        assertNotNull(recommendedDocs);
        assertEquals(5, recommendedDocs.size()); // Đảm bảo vẫn trả về đủ 5 sách
    }
}