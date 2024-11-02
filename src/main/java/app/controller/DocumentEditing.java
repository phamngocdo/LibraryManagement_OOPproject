package app.controller;

import app.base.Admin;
import app.base.Document;
import app.database.DocumentDAO;
import app.run.App;
import app.service.GoogleBookAPI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.concurrent.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DocumentEditing {

    @FXML
    private Label functionLabel, resultLabel;

    @FXML
    private TextField idField, titleField, publisherField, publishedDateField, authorField,
            categoryField, quantityField, remainingField, averageScoreField, pageCountField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private ProgressIndicator progressIndicator;

    private Document doc;

    @FXML
    void initialize() {
        if (doc != null) { // Nếu doc khác null tức là đang chỉnh sửa
            functionLabel.setText("Chỉnh sửa thông tin tài liệu");
            // Điền các trường thông tin từ tài liệu
            idField.setText(doc.getId());
            titleField.setText(doc.getTitle());
            publisherField.setText(doc.getPublisher());
            publishedDateField.setText(doc.getPublishedDate());
            authorField.setText(doc.getAuthorsToString()); // Sử dụng phương thức getAuthorsToString
            categoryField.setText(doc.getCategoriesToString()); // Sử dụng phương thức getCategoriesToString
            quantityField.setText(String.valueOf(doc.getQuantity()));
            remainingField.setText(String.valueOf(doc.getRemaining()));
            averageScoreField.setText(String.valueOf(doc.getAverageScore()));
            pageCountField.setText(String.valueOf(doc.getPageCount()));
            descriptionArea.setText(doc.getDescription());
        } else {
            functionLabel.setText("Thêm tài liệu");
        }
        progressIndicator.setVisible(false);
    }

    @FXML
    private void onSaveDocument() {
        try {
            // Chuyển đổi dữ liệu đầu vào thành số
            int quantity = Integer.parseInt(quantityField.getText());
            int remaining = Integer.parseInt(remainingField.getText());
            double averageScore = Double.parseDouble(averageScoreField.getText());

            // Kiểm tra số lượng tổng
            if (quantity < 0) {
                resultLabel.setText("Số lượng sách phải lớn hơn hoặc bằng 0.");
                return;
            }

            // Kiểm tra số lượng còn lại
            if (remaining < 0 || remaining > quantity) {
                resultLabel.setText("Số lượng sách còn lại phải từ 0 đến tổng số lượng.");
                return;
            }

            // Kiểm tra giá trị điểm trung bình
            if (averageScore < 0 || averageScore > 5) {
                resultLabel.setText("Điểm trung bình phải từ 0 đến 5.");
                return;
            }

            if (doc != null) {
                // Cập nhật tài liệu hiện tại
                doc.setTitle(titleField.getText());
                doc.setPublisher(publisherField.getText());
                doc.setPublishedDate(publishedDateField.getText());
                doc.setQuantity(quantity);
                doc.setRemaining(remaining);
                doc.setAverageScore(averageScore);
                doc.setPageCount(Integer.parseInt(pageCountField.getText()));
                doc.setDescription(descriptionArea.getText());

                // Cập nhật tài liệu và hiển thị thông báo
                String updateResult = ((Admin) App.currentUser).updateDocument(doc);
                resultLabel.setText(updateResult.isEmpty() ? "Thành công" : updateResult);
            } else {
                // Tạo tài liệu mới và thêm vào hệ thống
                Document newDoc = new Document(
                        idField.getText(),
                        titleField.getText(),
                        quantity,
                        remaining,
                        0, // Số lượng đánh giá ban đầu
                        averageScore,
                        Integer.parseInt(pageCountField.getText()),
                        descriptionArea.getText(),
                        publisherField.getText(),
                        publishedDateField.getText(),
                        "" // Đường dẫn hình ảnh trống
                );

                // Thêm tài liệu mới và hiển thị thông báo
                String addResult = ((Admin) App.currentUser).addDocument(newDoc);
                resultLabel.setText(addResult.isEmpty() ? "Thành công" : addResult);
            }

        } catch (NumberFormatException e) {
            resultLabel.setText("Vui lòng nhập đúng định dạng số cho số lượng và điểm trung bình.");
        }
    }

    @FXML
    private void onDeleteDocument() {
        if (doc != null) {
            resultLabel.setText(((Admin) App.currentUser).removeDocument(doc.getId()));
            resultLabel.setText("Xóa tài liệu thành công.");
        } else {
            resultLabel.setText("Tài liệu không tồn tại để xóa.");
        }
    }

    @FXML
    private void onCancel() {
        if (doc != null) {
            // Điền lại các trường thông tin từ tài liệu hiện tại
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
        } else {
            // Xóa tất cả các trường
            idField.clear();
            titleField.clear();
            publisherField.clear();
            publishedDateField.clear();
            authorField.clear();
            categoryField.clear();
            quantityField.clear();
            remainingField.clear();
            averageScoreField.clear();
            pageCountField.clear();
            descriptionArea.clear();
            resultLabel.setText("");
        }
    }

    private List<String> getAuthorsFromField() {
        String authorsText = authorField.getText();
        if (authorsText == null || authorsText.isEmpty()) {
            return new ArrayList<>(); // Trả về danh sách rỗng nếu không có tác giả
        }
        // Phân tách chuỗi thành danh sách tác giả (giả sử phân tách bằng dấu phẩy)
        return Arrays.asList(authorsText.split("\\s*,\\s*")); // Xóa khoảng trắng xung quanh
    }

    private List<String> getCategoriesFromField() {
        String categoriesText = categoryField.getText();
        if (categoriesText == null || categoriesText.isEmpty()) {
            return new ArrayList<>(); // Trả về danh sách rỗng nếu không có thể loại
        }
        // Phân tách chuỗi thành danh sách thể loại (giả sử phân tách bằng dấu phẩy)
        return Arrays.asList(categoriesText.split("\\s*,\\s*")); // Xóa khoảng trắng xung quanh
    }

    @FXML
    private void onGetAPIInfo() throws IOException {
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
                averageScoreField.setText(String.format("%.2f", doc.getAverageScore()));
                pageCountField.setText(String.valueOf(doc.getPageCount()));
                descriptionArea.setText(doc.getDescription());
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

    public void setDocument(Document doc) {
        this.doc = doc;
    }
}
