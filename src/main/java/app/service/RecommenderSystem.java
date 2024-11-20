package app.service;

import app.base.*;
import app.dao.AuthorDAO;
import app.dao.CategoryDAO;
import app.dao.DocumentDAO;
import app.dao.ReceiptDAO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class RecommenderSystem {

    public static ArrayList<Document> getRecommendDocFromMember(Member member) {
        ArrayList<Document> recommendedDocs = new ArrayList<>();

        // Lấy danh sách các biên nhận (receipts) của member
        ArrayList<Receipt> receipts = ReceiptDAO.getAllReceiptFromMemberId(member.getId());

        // Nếu người dùng chưa mượn sách nào, lấy 5 sách ngẫu nhiên từ danh sách tài liệu có xếp hạng cao
        if (receipts.isEmpty()) {
            return getTopRatingDocuments(5);
        }

        // Sử dụng HashSet để tránh trùng lặp tài liệu khi lấy cùng tác giả hoặc thể loại
        HashSet<String> recommendedDocIds = new HashSet<>();

        // Duyệt qua từng biên nhận (sách đã mượn)
        for (Receipt receipt : receipts) {
            String docId = receipt.getDocId();

            // Lấy các tác giả của tài liệu
            ArrayList<Author> authors = AuthorDAO.getAllAuthorFromDocId(docId);
            for (Author author : authors) {
                // Lấy các tài liệu cùng tác giả
                ArrayList<Document> docsByAuthor = DocumentDAO.getAllDocumentFromAuthor(author.getId());
                for (Document doc : docsByAuthor) {
                    if (!recommendedDocIds.contains(doc.getId())) {
                        recommendedDocs.add(doc);
                        recommendedDocIds.add(doc.getId());
                    }
                }
            }

            // Lấy các thể loại của tài liệu
            ArrayList<Category> categories = CategoryDAO.getAllCategoryFromDocId(docId);
            for (Category category : categories) {
                // Lấy các tài liệu cùng thể loại
                ArrayList<Document> docsByCategory = DocumentDAO.getAllDocumentFromCategory(category.getId());
                for (Document doc : docsByCategory) {
                    if (!recommendedDocIds.contains(doc.getId())) {
                        recommendedDocs.add(doc);
                        recommendedDocIds.add(doc.getId());
                    }
                }
            }
        }

        // Nếu số lượng tài liệu gợi ý chưa đủ 5, lấy thêm từ danh sách các tài liệu xếp hạng cao
        if (recommendedDocs.size() < 5) {
            int remaining = 5 - recommendedDocs.size();
            ArrayList<Document> topRatingDocs = getTopRatingDocuments(remaining);
            recommendedDocs.addAll(topRatingDocs);
        }

        // Trộn lại danh sách để tránh thiên lệch
        Collections.shuffle(recommendedDocs);

        // Trả về danh sách 5 tài liệu được đề xuất
        return new ArrayList<>(recommendedDocs.subList(0, Math.min(recommendedDocs.size(), 5)));
    }

    private static ArrayList<Document> getTopRatingDocuments(int limit) {
        // Lấy các tài liệu có xếp hạng cao, có thể gọi từ DocumentDAO với query tùy chỉnh
        return DocumentDAO.getBestRatingDocuments(limit);
    }
}
