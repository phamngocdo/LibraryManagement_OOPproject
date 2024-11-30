package app.controller;

import app.base.Admin;
import app.base.Author;
import app.base.Category;
import app.base.Document;
import app.run.App;
import app.service.GoogleBookAPI;
import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.concurrent.Task;
import java.util.ArrayList;
import java.util.Optional;

public class DocumentEditing {

    @FXML
    private Label functionLabel, resultLabel;

    @FXML
    private TextField titleField, isbnField, publisherField, publishedDateField, authorField, imageUrlFIeld;

    @FXML
    private TextField categoryField, quantityField, ratingCountField, remainingField, averageScoreField, pageCountField;

    @FXML
    private JFXTextArea descriptionArea;

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
        resultLabel.setText("");
        progressIndicator.setVisible(false);
    }

    private void populateFields() {
        if (doc != null) {
            functionLabel.setText("Chỉnh sửa thông tin tài liệu");
            titleField.setText(doc.getTitle());
            isbnField.setText(doc.getIsbn());
            publisherField.setText(doc.getPublisher());
            publishedDateField.setText(doc.getPublishedDate());
            authorField.setText(doc.getAuthorsToString());
            categoryField.setText(doc.getCategoriesToString());
            quantityField.setText(String.valueOf(doc.getQuantity()));
            ratingCountField.setText(String.valueOf(doc.getRatingCount()));
            remainingField.setText(String.valueOf(doc.getRemaining()));
            averageScoreField.setText(String.valueOf(doc.getAverageScore()));
            pageCountField.setText(String.valueOf(doc.getPageCount()));
            descriptionArea.setText(doc.getDescription());
            imageUrlFIeld.setText(doc.getImageUrl());
        } else {
            ratingCountField.setText("0");
            remainingField.setText("0");
        }
    }

    @FXML
    private void onSaveDocument() {
        setStyleForResultLabel("error");
        String title = titleField.getText();
        String isbn = isbnField.getText();
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

        if (title.isEmpty() || isbn.isEmpty() || publisher.isEmpty() || publishedDate.isEmpty()
                || authorText.isEmpty() || categoryText.isEmpty() || quantityText.isEmpty() || remainingText.isEmpty()
                || ratingCountText.isEmpty() || averageScoreText.isEmpty() || pageCountText.isEmpty()
                || description.isEmpty() || imageUrl.isEmpty()) {
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

            authorText = authorText.trim();
            String[] authorsStringList = authorText.isEmpty() ? new String[0] : authorText.split("\\s*,\\s*");
            ArrayList<Author> authors = new ArrayList<>();
            for (String str : authorsStringList) {
                if (!str.isEmpty()) {
                    authors.add(new Author(null, str.trim()));
                }
            }

            categoryText = categoryText.trim();
            String[] categoryStringList = categoryText.isEmpty() ? new String[0] : categoryText.split("\\s*,\\s*");
            ArrayList<Category> categories = new ArrayList<>();
            for (String str : categoryStringList) {
                if (!str.isEmpty()) {
                    categories.add(new Category(null, str.trim()));
                }
            }

            String docId = doc == null ? "" : doc.getId();
            Document newDoc = new Document(docId, title, isbn, quantity, remaining, ratingCount,
                    averageScore, pageCount, description, publisher, publishedDate,
                    imageUrl, authors, categories, new ArrayList<>());

            if (functionLabel.getText().equals("Thêm tài liệu")) {
                resultLabel.setText(((Admin) App.currentUser).addDocument(newDoc));
                if(resultLabel.getText().equals("Thêm tài liệu thành công")) {
                    doc = newDoc;
                    setStyleForResultLabel("success");
                }
            } else {
                resultLabel.setText(((Admin) App.currentUser).updateDocument(newDoc));
                setStyleForResultLabel("success");
            }
        } catch (NumberFormatException e) {
            resultLabel.setText("Vui lòng nhập đúng định dạng số cho các trường số.");
        }
    }

    @FXML
    private void onDeleteDocument() {
        ButtonType buttonType = new ButtonType("OK");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xóa tài liệu");
        alert.setContentText("Bạn có chắc muốn xóa tài liệu " + doc.getTitle() + " có mã ISBN " + doc.getIsbn());
        alert.setHeaderText(null);
        alert.getButtonTypes().setAll(buttonType);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonType) {
            if (doc != null) {
                setStyleForResultLabel("success");
                resultLabel.setText(((Admin) App.currentUser).removeDocument(doc.getId()));
            } else {
                resultLabel.setText("Tài liệu không tồn tại để xóa");
                setStyleForResultLabel("error");
            }
        }
    }

    @FXML
    private void onGetAPIInfo() {
        String isbn = isbnField.getText();
        if (isbn.isEmpty()) {
            resultLabel.setText("Vui lòng nhập mã ISBN của sách");
            setStyleForResultLabel("error");
            return;
        }

        progressIndicator.setVisible(true);
        resultLabel.setText("");

        Task<Document> task = new Task<>() {
            @Override
            protected Document call() throws Exception {
                return GoogleBookAPI.getDocFromIsbn(isbn);
            }
        };

        task.setOnSucceeded(event -> {
            doc = task.getValue();
            if (doc != null) {
                titleField.setText(doc.getTitle());
                isbnField.setText(doc.getIsbn());
                publisherField.setText(doc.getPublisher());
                publishedDateField.setText(doc.getPublishedDate());
                authorField.setText(doc.getAuthorsToString());
                categoryField.setText(doc.getCategoriesToString());
                quantityField.setText(String.valueOf(doc.getQuantity()));
                remainingField.setText(String.valueOf(doc.getRemaining()));
                ratingCountField.setText(String.valueOf(doc.getRatingCount()));
                averageScoreField.setText(String.format("%.2f", doc.getAverageScore()));
                pageCountField.setText(String.valueOf(doc.getPageCount()));
                descriptionArea.setText(doc.getDescription());
                imageUrlFIeld.setText(doc.getImageUrl());
                resultLabel.setText("Đã lấy dữ liệu từ API");
                setStyleForResultLabel("success");
            } else {
                resultLabel.setText("Không có kết quả tương ứng từ API");
                setStyleForResultLabel("error");
            }
            progressIndicator.setVisible(false);
        });

        task.setOnFailed(event -> {
            resultLabel.setText("Lỗi khi lấy dữ liệu từ API");
            setStyleForResultLabel("error");
            progressIndicator.setVisible(false);
        });

        new Thread(task).start();
    }

    private void setStyleForResultLabel(String result) {
        if ("success".equals(result)) {
            resultLabel.getStyleClass().clear();
            resultLabel.getStyleClass().add("suc-result-label");
        } else if ("error".equals(result)) {
            resultLabel.getStyleClass().clear();
            resultLabel.getStyleClass().add("er-result-label");
        }
    }
}
