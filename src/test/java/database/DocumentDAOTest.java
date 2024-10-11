package database;

import app.base.Document;
import app.database.DatabaseManagement;
import app.database.DocumentDAO;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DocumentDAOTest {

    @BeforeEach
    void setup() {
        DatabaseManagement.setConnection();
    }

    @Test
    public void testAddDocument() {
        // Test thêm một tài liệu hợp lệ
        Document validDoc = new Document("5", "Valid Title", "123456789", 10, 8, 5, 4.5, "valid_image.jpg");
        DocumentDAO.addDocument(validDoc);
        Document fetchedDoc = DocumentDAO.getDocFromId(validDoc.getId());
        assertNotNull(fetchedDoc);
        assertEquals("Valid Title", fetchedDoc.getTitle());

    }

    @Test
    public void testGetAllDoc() {
        // Test lấy tất cả tài liệu khi có tài liệu trong DB
        ArrayList<Document> documents = DocumentDAO.getAllDoc();
        assertNotNull(documents);
        assertTrue(documents.size() > 0); // Giả sử có ít nhất 1 tài liệu trong DB

        // Thêm một tài liệu và kiểm tra
        Document docToCheck = new Document(null, "New Document", "222222222", 3, 3, 0, 4.5, "new_image.jpg");
        DocumentDAO.addDocument(docToCheck);
        documents = DocumentDAO.getAllDoc();
        assertTrue(documents.size() > 0); // Đảm bảo có tài liệu

        // Xóa tài liệu vừa thêm và kiểm tra
        DocumentDAO.removeDocument(docToCheck.getId());
        documents = DocumentDAO.getAllDoc();
        assertFalse(documents.contains(docToCheck)); // Đảm bảo tài liệu đã bị xóa
    }

    @Test
    public void testRemoveDocument() {
        // Test xóa một tài liệu có tồn tại
        Document docToRemove = new Document(null, "Title to Remove", "333333333", 5, 5, 2, 3.5, "remove_image.jpg");
        DocumentDAO.addDocument(docToRemove);
        DocumentDAO.removeDocument(docToRemove.getId());
        Document fetchedDoc = DocumentDAO.getDocFromId(docToRemove.getId());
        assertNull(fetchedDoc);  // Đảm bảo tài liệu đã bị xóa

        // Test xóa tài liệu không tồn tại
        assertThrows(RuntimeException.class, () -> DocumentDAO.removeDocument("non_existing_id"));

        // Test xóa tài liệu đã xóa
        DocumentDAO.removeDocument(docToRemove.getId()); // Đã xóa trước đó, không có ảnh hưởng gì
        fetchedDoc = DocumentDAO.getDocFromId(docToRemove.getId());
        assertNull(fetchedDoc); // Đảm bảo tài liệu vẫn không có
    }

    @Test
    public void testGetBestRatingDocuments() {
        // Test lấy tài liệu có điểm đánh giá cao nhất
        ArrayList<Document> bestRatingDocs = DocumentDAO.getBestRatingDocuments(3);
        assertNotNull(bestRatingDocs);
        assertTrue(bestRatingDocs.size() > 0); // Giả sử có ít nhất 1 tài liệu

        // Kiểm tra rằng danh sách tài liệu có đánh giá cao nhất được sắp xếp đúng
        assertTrue(bestRatingDocs.get(0).getAverageScore() >= bestRatingDocs.get(1).getAverageScore());

        // Test với số lượng tài liệu lớn hơn số tài liệu trong DB
        bestRatingDocs = DocumentDAO.getBestRatingDocuments(100);
        assertNotNull(bestRatingDocs);
        assertTrue(bestRatingDocs.size() > 0); // Vẫn có tài liệu

        // Test khi không có tài liệu trong DB
        if (!bestRatingDocs.isEmpty()) {
            DocumentDAO.removeDocument(bestRatingDocs.get(0).getId()); // Xóa tài liệu
        }
        bestRatingDocs = DocumentDAO.getBestRatingDocuments(1);
        assertTrue(bestRatingDocs.size() == 0); // Không còn tài liệu nào
    }

    @Test
    public void testGetDocumentFromAuthor() {
        // Test tìm tài liệu từ tác giả có tài liệu
        ArrayList<Document> documentsFromAuthor = DocumentDAO.getAllDocumentFromAuthor("J.K. Rowling");
        assertNotNull(documentsFromAuthor);
        assertTrue(documentsFromAuthor.size() > 0);

        // Test với tác giả không có tài liệu nào
        documentsFromAuthor = DocumentDAO.getAllDocumentFromAuthor("Unknown Author");
        assertNotNull(documentsFromAuthor);
        assertTrue(documentsFromAuthor.isEmpty()); // Không có tài liệu

        // Test với tên tác giả không hợp lệ
        documentsFromAuthor = DocumentDAO.getAllDocumentFromAuthor("");
        assertNotNull(documentsFromAuthor);
        assertTrue(documentsFromAuthor.isEmpty()); // Không có tài liệu
    }

    @Test
    public void testGetDocumentFromCategory() {
        // Test tìm tài liệu từ thể loại có tài liệu
        ArrayList<Document> documentsFromCategory = DocumentDAO.getAllDocumentFromCategory("Fiction");
        assertNotNull(documentsFromCategory);
        assertTrue(documentsFromCategory.size() > 0);

        // Test với thể loại không có tài liệu nào
        documentsFromCategory = DocumentDAO.getAllDocumentFromCategory("Unknown Category");
        assertNotNull(documentsFromCategory);
        assertTrue(documentsFromCategory.isEmpty()); // Không có tài liệu

        // Test với tên thể loại không hợp lệ
        documentsFromCategory = DocumentDAO.getAllDocumentFromCategory("");
        assertNotNull(documentsFromCategory);
        assertTrue(documentsFromCategory.isEmpty()); // Không có tài liệu
    }
}
