package app.controller;

import app.base.*;
import app.database.DocumentDAO;
import app.database.RatingDAO;
import app.database.ReceiptDAO;
import app.run.App;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;

public class DocumentInfo {
    @FXML
    private Pane functionPane, infoPane, memberRatingPane;

    @FXML
    private Label authorsLabel, averageScoreLabel, categoriesLabel, idLabel, pageCountLabel;

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
        if (App.currentUser instanceof Admin) {
            memberRatingPane.setVisible(false);
            editingButton.setVisible(true);
            borrowingButton.setVisible(false);
        } else if (App.currentUser instanceof Member) {
            memberRatingPane.setVisible(true);
            editingButton.setVisible(false);
            /**
             * Kiểm tra currentDoc có được mượn không
             * chưa mượn thì hiển thị nút mượn và ngược lại
             */
            // Lấy biên lai từ database
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
    private void sendRating() {
        /**
         * Hàm này đọc số sao + nhận xét và sử dụng (Member) App.currentUser.addRating(rating);
         */
        try {
            String comment = memberComment.getText();

            Rating newRating = new Rating(
                    null,
                    App.currentUser.getId(),
                    currentDoc.getId(),
                    selectedStar,
                    comment
            );
            // Lưu đánh giá vào cơ sở dữ liệu
            RatingDAO.addRating(newRating);
            // Thêm đánh giá mới vào giao diện
            addRatingIntoVBox(newRating);
            // Xóa nhận xét sau khi gửi
            memberComment.clear();
            displayRatedStar(0, starRatingHBox); // Đặt lại HBox về trạng thái ban đầu

            System.out.println("Đánh giá đã được gửi thành công!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
            throw new RuntimeException(e);
        }
    }

    private void setUpInfo() {
        /** Hàm hiển thị các thông tin về sách, đánh giá (nhớ sử dụng hàm addRatingIntoVBox bên dưới) sử dụng đa luồng:
         * Ban đầu: infoPane.setVisible(false), progressIndicator.setVisible(true)
         * Khi nhận được các giá trị thì setText cho các mục và đổi ngược lại 2 cái visible ở trên
         * Chú ý sử dụng hàm getCatogories và getAuthors
         */
        // Ẩn bảng thông tin và hiển thị ProgressIndicator khi đang tải dữ liệu
        infoPane.setVisible(false);
        progressIndicator.setVisible(true);

        // Sử dụng một luồng mới để xử lý tải thông tin tài liệu
        new Thread(() -> {
            try {
                // Lấy thông tin tài liệu hiện tại
                Document doc = currentDoc;
                // Cập nhật giao diện trong JavaFX Application Thread
                Platform.runLater(() -> {
                    // Hiển thị thông tin tài liệu
                    assert doc != null;
                    titleLabel.setText(doc.getTitle());
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
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private void setUpRatingHBox() {
        /** Hàm này set các sự kiện khi người dùng chọn sao trong HBox
         *  Sử dụng hàm displayRatedStar khi người dùng chọn sao
         */
        // Lặp qua từng ImageView trong HBox (5 sao)
        for (int i = 0; i < starRatingHBox.getChildren().size(); i++) {
            final int starIndex = i + 1; // Vị trí sao (1 đến 5)

            ImageView starImageView = (ImageView) starRatingHBox.getChildren().get(i);

            starImageView.setOnMouseClicked(event -> {
                // Cập nhật giao diện các sao đã chọn
                displayRatedStar(starIndex, starRatingHBox);
                selectedStar = starIndex;//lưu số sao đã đánh giá
                // Bạn có thể thêm logic khác (ví dụ: lưu số sao đã đánh giá)
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
        Label memberIdlabel = new Label(rating.getUserId());
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
