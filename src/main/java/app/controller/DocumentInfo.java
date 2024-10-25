package app.controller;

import app.base.*;
import app.run.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
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
    private Label authorsLabel, averageScoreLabel, categoriesLabel, idLabel, isbnLabel, languageLabel, pageCountLabel;

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
    private Button ratingSendingButton, borrowingButton, returnButton, editingButton;

    @FXML
    private HBox starRatingHBox;

    @FXML
    private ProgressIndicator progressIndicator;

    public static Document currentDoc;

    @FXML
    private void initialize() {
        if (App.currentUser instanceof Admin) {
            memberRatingPane.setVisible(false);
            editingButton.setVisible(true);
            borrowingButton.setVisible(false);
            returnButton.setVisible(false);
        } else if (App.currentUser instanceof Member) {
            memberRatingPane.setVisible(true);
            editingButton.setVisible(false);
            /**
             * Kiểm tra currentDoc có được mượn không
             * chưa mượn thì hiển thị nút mượn và ngược lại
             */
        }
        setUpInfo();
    }

    @FXML
    private void sendRating() {
        /**
         * Hàm này đọc số sao + nhận xét và sử dụng (Member) App.currentUser.addRating(rating);
         */
    }

    @FXML
    private void borrowDoc() {
        ((Member) App.currentUser).borrowDocument(currentDoc);
        borrowingButton.setVisible(false);
        returnButton.setVisible(true);
    }

    @FXML
    private void returnDoc() {
        ((Member) App.currentUser).returnDocument(currentDoc);
        borrowingButton.setVisible(true);
        returnButton.setVisible(false);
    }

    @FXML
    private void editDoc() {
        try {
            URL url = getClass().getResource("/fxml/DocumentEdit.fxml");
            if (url != null) {
                Parent page = FXMLLoader.load(url);
                functionPane.getChildren().clear();
                functionPane.getChildren().add(page);
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
    }

    private void setUpRatingHBox() {
        /** Hàm này set các sự kiện khi người dùng chọn sao trong HBox
         *  Sử dụng hàm displayRatedStar khi người dùng chọn sao
         */
    }

    private void displayRatedStar(int numberRatedStar, HBox starRatingHBox) {
        for (int i = 0; i < 5; i++) {
            ImageView imageView = (ImageView) starRatingHBox.getChildren().get(i);
            if (i == numberRatedStar - 1) {
                imageView.getStyleClass().add("rated-star");
                imageView.getStyleClass().clear();
            } else {
                imageView.getStyleClass().add("un-rated-star");
                imageView.getStyleClass().clear();
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
