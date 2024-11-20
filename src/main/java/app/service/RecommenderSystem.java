package app.service;

import app.base.*;
import app.run.App;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;


public class RecommenderSystem {

    public static ArrayList<Document> getRecommendDocFromMember(Member member, int number) {
        ArrayList<Document> recommendedDocs = new ArrayList<>();
        ArrayList<Receipt> receipts = member.getReceipts();
        if (receipts.isEmpty()) {
            recommendedDocs.addAll(member.seeTopRatingDoc(40));
        }

        HashSet<String> recommendedDocIds = new HashSet<>();

        for (Receipt receipt : receipts) {
            Document docOfReceipt = new Document(receipt.getDocId());
            ArrayList<Author> authors = docOfReceipt.getAuthors();
            for (Author author : authors) {
                ArrayList<Document> docsByAuthor = author.getAllDoc();
                for (Document doc : docsByAuthor) {
                    if (!recommendedDocIds.contains(doc.getId())) {
                        recommendedDocs.add(doc);
                        recommendedDocIds.add(doc.getId());
                    }
                }
            }

            ArrayList<Category> categories = docOfReceipt.getCategories();
            for (Category category : categories) {
                ArrayList<Document> docsByCategory = category.getAllDoc();
                for (Document doc : docsByCategory) {
                    if (!recommendedDocIds.contains(doc.getId())) {
                        recommendedDocs.add(doc);
                        recommendedDocIds.add(doc.getId());
                    }
                }
            }
        }

        if (recommendedDocs.size() < number) {
            recommendedDocs.addAll(member.seeTopRatingDoc(40));
        }
        Collections.shuffle(recommendedDocs);
        return new ArrayList<>(recommendedDocs.subList(0, Math.min(recommendedDocs.size(), 5)));
    }
}
