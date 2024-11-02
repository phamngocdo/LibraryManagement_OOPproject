package app.controller;

import app.base.Admin;
import app.base.Author;
import app.base.Category;
import app.base.Document;
import app.run.App;
import app.service.GoogleBookAPI;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.concurrent.Task;
import java.util.ArrayList;

public class DocumentEditing {

    @FXML
    private Label functionLabel, resultLabel;

    @FXML
    private TextField idField, titleField, publisherField, publishedDateField, authorField, imageUrlFIeld;

    @FXML
    private TextField categoryField, quantityField, ratingCountField, remainingField, averageScoreField, pageCountField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Button savingButton, deletingButton;

    @FXML
    private ProgressIndicator progressIndicator;

    private Document doc;

    public void setDocument(Document doc) {
        this.doc = doc;
        populateFields();
    }

    @FXML
    void initialize() {
        functionLabel.setText("Thêm tài liệu");
        progressIndicator.setVisible(false);
    }

    private void populateFields() {
        if (doc != null) { // Nếu doc khác null tức là đang chỉnh sửa
            functionLabel.setText("Chỉnh sửa thông tin tài liệu");
            idField.setText(doc.getId());
            titleField.setText(doc.getTitle());
            publisherField.setText(doc.getPublisher());
            publishedDateField.setText(doc.getPublishedDate());
            authorField.setText(doc.getAuthorsToString());
            categoryField.setText(doc.getCategoriesToString());
            quantityField.setText(String.valueOf(doc.getQuantity()));
            remainingField.setText(String.valueOf(doc.getRemaining()));
            averageScoreField.setText(String.valueOf(doc.getAverageScore()));
            pageCountField.setText(String.valueOf(doc.getPageCount()));
            descriptionArea.setText(doc.getDescription());
            imageUrlFIeld.setText(doc.getImageUrl());
        }
    }

    @FXML
    private void onSaveDocument() {
        String id = idField.getText();
        String title = titleField.getText();
        String publisher = publisherField.getText();
        String publishedDate = publishedDateField.getText();
        String authorText = authorField.getText();
        String categoryText = categoryField.getText();
        String quantityText = quantityField.getText();
        String remainingText = remainingField.getText();
        String ratingCountText = ratingCountField.getText();
        String averageScoreText = averageScoreField.getText();
        String pageCountText = pageCountField.getText();
        String description = descriptionArea.getText();
        String imageUrl = imageUrlFIeld.getText();

        if (id.isEmpty() || title.isEmpty() || publisher.isEmpty() || publishedDate.isEmpty()
                || authorText.isEmpty() || categoryText.isEmpty() || quantityText.isEmpty() || remainingText.isEmpty()
                || ratingCountText.isEmpty() || averageScoreText.isEmpty() || pageCountText.isEmpty() || description.isEmpty() || imageUrl.isEmpty()) {
            resultLabel.setText("Bạn cần điền đầy đủ thông tin");
            return;
        }
        try {
            int quantity = Integer.parseInt(quantityText);
            int remaining = Integer.parseInt(remainingText);
            int ratingCount = Integer.parseInt(ratingCountText);
            double averageScore = Double.parseDouble(averageScoreText);
            int pageCount = Integer.parseInt(pageCountText);

            if (averageScore < 0 || averageScore > 5) {
                resultLabel.setText("Điểm đánh giá phải nằm trong khoảng từ 0 đến 5");
                return;
            }

            if (remaining < 0 || remaining > quantity) {
                resultLabel.setText("Nhập lại số lượng sách cho phù hợp");
                return;
            }

            String[] authorsStringList = authorText.split("\\s*,\\s*");
            ArrayList<Author> authors = new ArrayList<>();
            for (String str : authorsStringList) {
                authors.add(new Author("", str));
            }

            String[] categoryStringList = categoryText.split("\\s*,\\s*");
            ArrayList<Category> categories = new ArrayList<>();
            for (String str : categoryStringList) {
                categories.add(new Category("", str));
            }

            Document newDoc = new Document(id, title, quantity, remaining, ratingCount,
                    averageScore, pageCount, description, publisher, publishedDate,
                    imageUrl, authors, categories, null);

            if (functionLabel.getText().equals("Thêm tài liệu")) {
                resultLabel.setText(((Admin) App.currentUser).addDocument(newDoc));
                if(resultLabel.getText().equals("Thêm tài liệu thành công")) {
                    doc = newDoc;
                }
            } else {
                resultLabel.setText(((Admin) App.currentUser).updateDocument(doc));
            }
        } catch (NumberFormatException e) {
            resultLabel.setText("Vui lòng nhập đúng định dạng số cho các trường số.");
        }
    }

    @FXML
    private void onDeleteDocument() {
        if (doc != null) {
            resultLabel.setText(((Admin) App.currentUser).removeDocument(doc.getId()));
        } else {
            resultLabel.setText("Tài liệu không tồn tại để xóa.");
        }
    }

    @FXML
    private void onGetAPIInfo() {
        String docId = idField.getText();
        if (docId.isEmpty()) {
            resultLabel.setText("Vui lòng nhập ID sách.");
            return;
        }

        progressIndicator.setVisible(true);
        resultLabel.setText("");

        Task<Document> task = new Task<>() {
            @Override
            protected Document call() throws Exception {
                return GoogleBookAPI.getDocFromId(docId);
            }
        };

        task.setOnSucceeded(event -> {
            doc = task.getValue();
            if (doc != null) {
                // Điền lại các trường thông tin từ tài liệu API
                titleField.setText(doc.getTitle());
                publisherField.setText(doc.getPublisher());
                publishedDateField.setText(doc.getPublishedDate());
                authorField.setText(doc.getAuthorsToString()); // Sử dụng phương thức getAuthorsToString
                categoryField.setText(doc.getCategoriesToString()); // Sử dụng phương thức getCategoriesToString
                quantityField.setText(String.valueOf(doc.getQuantity()));
                remainingField.setText(String.valueOf(doc.getRemaining()));
                ratingCountField.setText(String.valueOf(doc.getRatingCount()));
                averageScoreField.setText(String.format("%.2f", doc.getAverageScore()));
                pageCountField.setText(String.valueOf(doc.getPageCount()));
                descriptionArea.setText(doc.getDescription());
                imageUrlFIeld.setText(doc.getImageUrl());
                resultLabel.setText("Đã lấy dữ liệu từ API.");
            } else {
                resultLabel.setText("Không có kết quả tương ứng từ API.");
            }
            progressIndicator.setVisible(false);
        });

        task.setOnFailed(event -> {
            resultLabel.setText("Lỗi khi lấy dữ liệu từ API.");
            progressIndicator.setVisible(false);
        });

        new Thread(task).start();
    }
}
