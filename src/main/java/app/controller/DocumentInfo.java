package app.controller;

import app.base.*;
import app.run.App;
import javafx.application.Platform;
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
    private ScrollPane scrollPane;

    @FXML
    private Pane functionPane, infoPane, memberRatingPane;

    @FXML
    private Label authorsLabel, isbnLabel, averageScoreLabel, categoriesLabel, pageCountLabel;

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
    private Button borrowingButton, editingButton;

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
            memberRatingPane.setDisable(true);
            editingButton.setVisible(true);
            borrowingButton.setVisible(false);
        } else if (App.currentUser instanceof Member) {
            memberRatingPane.setDisable(false);
            editingButton.setVisible(false);

            borrowingButton.setVisible(true);
            for (Receipt receipt : ((Member) App.currentUser).getReceipts()) {
                if (receipt.getDocId().equals(currentDoc.getId()) && receipt.getStatus().equals(STATUS_NOT_RETURNED)) {
                    borrowingButton.setVisible(false);
                    break;
                }
            }

            Rating rating = currentDoc.getRatings()
                    .stream()
                    .filter(r -> r.getMemberId().equals(App.currentUser.getId()))
                    .findFirst()
                    .orElse(null);
            memberRatingPane.setDisable(rating != null);
        }
        setUpInfo();
        setUpRatingHBox();
    }

    @FXML
    private void goBackToPrevPane() {
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
        ((Member) App.currentUser).rateDocument(newRating, currentDoc);
        memberComment.clear();
        displayRatedStar(0, starRatingHBox);
        starRatingHBox.setDisable(true);
        setUpInfo();
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
        infoPane.setVisible(false);
        progressIndicator.setVisible(true);
        new Thread(() -> {
            Document doc = currentDoc;
            Platform.runLater(() -> {
                assert doc != null;
                titleLabel.setText(doc.getTitle());
                isbnLabel.setText(doc.getIsbn());
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
                docImage.setImage(doc.loadImage());
                doc.getRatings().forEach(this::addRatingIntoVBox);
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
        pane.getStyleClass().add("rating-pane");
        pane.setPrefWidth(911);

        Member member = Member.loadFromId(rating.getMemberId());
        Label memberUserNameLabel = new Label(member.getUsername());
        memberUserNameLabel.getStyleClass().add("gen-label");
        memberUserNameLabel.setLayoutX(16);
        memberUserNameLabel.setLayoutY(14);

        TextArea commentArea = new TextArea(rating.getComment());
        commentArea.getStyleClass().add("rating-text-area");
        commentArea.setEditable(false);
        commentArea.setWrapText(true);
        commentArea.setLayoutX(6);
        commentArea.setLayoutY(65);
        commentArea.setPrefHeight(computeTextAreaHeight(commentArea));

        HBox starHBox = new HBox();
        starHBox.setLayoutX(16);
        starHBox.setLayoutY(37);
        for (int i = 0; i < 5; i++) {
            starHBox.getChildren().add(new ImageView());
        }
        displayRatedStar(rating.getRatingScore(), starHBox);
        otherRatingVBox.getChildren().add(pane);
        pane.getChildren().addAll(memberUserNameLabel, commentArea, starHBox);
    }

    private double computeTextAreaHeight(TextArea textArea) {
        double fontHeight = textArea.getFont().getSize();
        int lineCount = textArea.getText().split("\n").length;
        return Math.max(50, fontHeight * lineCount + 20);
    }
}
