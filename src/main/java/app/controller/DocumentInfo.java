package app.controller;

import app.base.*;
import app.dao.ReceiptDAO;
import app.run.App;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;

public class DocumentInfo {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Pane functionPane, infoPane, memberRatingPane;

    @FXML
    private ImageView backButton;

    @FXML
    private Label authorsLabel, isbnLabel, averageScoreLabel, categoriesLabel, idLabel, pageCountLabel;

    @FXML
    private Label publisherLabel, remainingLabel, pulishedDateLabel, quantityLabel, ratingCountLabel, titleLabel;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private ImageView docImage;

    @FXML
    private VBox otherRatingVBox;

    @FXML
    private TextArea memberComment;

    @FXML
    private Button ratingSendingButton, borrowingButton, editingButton;

    @FXML
    private HBox starRatingHBox;

    @FXML
    private ProgressIndicator progressIndicator;

    public static Document currentDoc;

    private static final String STATUS_NOT_RETURNED = "not returned";
    private static final String STATUS_RETURNED = "returned";
    private int selectedStar;

    @FXML
    private void initialize() {
        scrollPane.setVisible(true);
        if (App.currentUser instanceof Admin) {
            memberRatingPane.setVisible(false);
            editingButton.setVisible(true);
            borrowingButton.setVisible(false);
        } else if (App.currentUser instanceof Member) {
            memberRatingPane.setVisible(true);
            editingButton.setVisible(false);
            Receipt receipt = ReceiptDAO.getReceiptFromDocAndMember(
                    currentDoc.getId(),
                    App.currentUser.getId()
            );

            if (receipt != null) {
                if (receipt.getStatus().equals(STATUS_NOT_RETURNED)) {
                    // Nếu đã mượn, hiển thị nút trả tài liệu
                    borrowingButton.setVisible(false);
                } else if (receipt.getStatus().equals(STATUS_RETURNED)) {
                    // Nếu chưa mượn, hiển thị nút mượn tài liệu
                    borrowingButton.setVisible(true);
                }
            } else {
                borrowingButton.setVisible(true);
            }
        }
        setUpInfo();
        setUpRatingHBox();
    }

    @FXML
    private void goBackToPrevPane(MouseEvent event) {
        Parent parent = scrollPane.getParent();
        if (parent instanceof Pane parentPane) {
            parentPane.getChildren().remove(scrollPane);
        }
        parent.setVisible(false);
    }

    @FXML
    private void sendRating() {
        String comment = memberComment.getText();
        Rating newRating = new Rating(
                "",
                App.currentUser.getId(),
                currentDoc.getId(),
                selectedStar,
                comment
        );
        // Lưu đánh giá vào cơ sở dữ liệu
        ((Member) App.currentUser).rateDocument(newRating);
        // Thêm đánh giá mới vào giao diện
        addRatingIntoVBox(newRating);
        // Xóa nhận xét sau khi gửi
        memberComment.clear();
        displayRatedStar(0, starRatingHBox); // Đặt lại HBox về trạng thái ban đầu
        System.out.println("Đánh giá đã được gửi thành công!");

    }

    @FXML
    private void borrowDoc() {
        ((Member) App.currentUser).borrowDocument(currentDoc);
        borrowingButton.setVisible(false);
    }

    @FXML
    private void editDoc() {
        try {
            URL url = getClass().getResource("/fxml/DocumentEdit.fxml");
            if (url != null) {
                FXMLLoader loader = new FXMLLoader(url);
                Parent page = loader.load();
                functionPane.getChildren().clear();
                functionPane.getChildren().add(page);
                DocumentEditing controller = loader.getController();
                controller.setDocument(currentDoc);
            }
        } catch (IOException e) {
            throw new RuntimeException("Không tìm thấy đường dẫn của DocumentEdit.fxml");
        }
    }

    private void setUpInfo() {
        // Ẩn bảng thông tin và hiển thị ProgressIndicator khi đang tải dữ liệu
        infoPane.setVisible(false);
        progressIndicator.setVisible(true);
        // Sử dụng một luồng mới để xử lý tải thông tin tài liệu
        new Thread(() -> {
            // Lấy thông tin tài liệu hiện tại
            Document doc = currentDoc;
            // Cập nhật giao diện trong JavaFX Application Thread
            Platform.runLater(() -> {
                // Hiển thị thông tin tài liệu
                assert doc != null;
                titleLabel.setText(doc.getTitle());
                isbnLabel.setText(doc.getIsbn());
                idLabel.setText(doc.getId());
                authorsLabel.setText(doc.getAuthorsToString());
                categoriesLabel.setText(doc.getCategoriesToString());
                descriptionTextArea.setText(doc.getDescription());
                pageCountLabel.setText(String.valueOf(doc.getPageCount()));
                publisherLabel.setText(doc.getPublisher());
                pulishedDateLabel.setText(doc.getPublishedDate());
                quantityLabel.setText(String.valueOf(doc.getQuantity()));
                remainingLabel.setText(String.valueOf(doc.getRemaining()));
                ratingCountLabel.setText(String.valueOf(doc.getRatingCount()));
                averageScoreLabel.setText(String.format("%.2f", doc.getAverageScore()));

                // Hiển thị hình ảnh tài liệu
                docImage.setImage(doc.loadImage());
                // Thêm các đánh giá vào VBox
                doc.getRatings().forEach(this::addRatingIntoVBox);
                // Sau khi tải xong, hiển thị bảng thông tin và ẩn ProgressIndicator
                infoPane.setVisible(true);
                progressIndicator.setVisible(false);
            });
        }).start();
    }

    private void setUpRatingHBox() {
        for (int i = 0; i < starRatingHBox.getChildren().size(); i++) {
            int starIndex = i + 1;
            ImageView starImageView = (ImageView) starRatingHBox.getChildren().get(i);
            starImageView.setOnMouseClicked(event -> {
                displayRatedStar(starIndex, starRatingHBox);
                selectedStar = starIndex;
                System.out.println("Người dùng đã đánh giá: " + starIndex + " sao");
            });
        }
    }

    private void displayRatedStar(int numberRatedStar, HBox starRatingHBox) {
        for (int i = 0; i < 5; i++) {
            ImageView imageView = (ImageView) starRatingHBox.getChildren().get(i);
            imageView.getStyleClass().clear();

            if (i < numberRatedStar) {
                imageView.getStyleClass().add("rated-star");
            } else {
                imageView.getStyleClass().add("un-rated-star");
            }
        }
    }

    private void addRatingIntoVBox(Rating rating) {
        Pane pane = new Pane();
        Label memberIdlabel = new Label(rating.getMemberId());
        Label commentLabel = new Label(rating.getComment());
        HBox starHBox = new HBox();
        for (int i = 0; i < 5; i++) {
            starHBox.getChildren().add(new ImageView());
        }
        displayRatedStar(rating.getRatingScore(), starHBox);
        otherRatingVBox.getChildren().add(pane);
        pane.getChildren().addAll(memberIdlabel, commentLabel, starHBox);
    }
}
