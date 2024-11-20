package app.controller;

import app.base.Document;
import app.base.Member;
import app.base.Receipt;
import app.run.App;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class BorrowingList {

    @FXML
    private VBox notReturnedDocVBox, returnedDocVBox;

    @FXML
    private Pane docInfoPane, bookshelfPane;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private void initialize() {
        docInfoPane.setVisible(false);
        notReturnedDocVBox.getChildren().clear();
        returnedDocVBox.getChildren().clear();

        ArrayList<Receipt> returnedList = new ArrayList<>();
        ArrayList<Receipt> notReturnedList = new ArrayList<>();
        for (Receipt receipt : ((Member) App.currentUser).getReceipts()) {
            if (receipt.getStatus().equals("returned")) {
                returnedList.add(receipt);
            } else if (receipt.getStatus().equals("not returned")) {
                notReturnedList.add(receipt);
            }
        }

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                ArrayList<HBox> returnedHBoxes = prepareData(returnedList);
                ArrayList<HBox> notReturnedHBoxes = prepareData(notReturnedList);
                Platform.runLater(() -> {
                    returnedDocVBox.getChildren().setAll(returnedHBoxes);
                    notReturnedDocVBox.getChildren().setAll(notReturnedHBoxes);
                    bookshelfPane.setVisible(true);
                    progressIndicator.setVisible(false);
                });
                return null;
            }
        };
        bookshelfPane.setVisible(false);
        progressIndicator.setVisible(true);
        new Thread(task).start();
    }

    private ArrayList<HBox> prepareData(ArrayList<Receipt> list) {
        ArrayList<HBox> hBoxes = new ArrayList<>();
        int hBoxCount = (int) Math.ceil((double) list.size() / 5);
        int listIndex = 0;

        for (int i = 0; i < hBoxCount; i++) {
            HBox hBox = new HBox();
            hBox.setSpacing(20);
            for (int j = 0; j < 5; j++) {
                if (listIndex == list.size()) {
                    break;
                }
                Receipt receipt = list.get(listIndex++);
                Document doc = new Document(receipt.getDocId());
                Pane pane = createDocPane(doc, receipt);
                hBox.getChildren().add(pane);
            }
            hBoxes.add(hBox);
        }
        return hBoxes;
    }

    private Pane createDocPane(Document doc, Receipt receipt) {
        Pane pane = new Pane();
        pane.getStyleClass().add("doc-pane-qr");

        ImageView docImage = new ImageView(doc.loadImage());
        docImage.setOnMouseClicked(event -> displayDetailDoc(doc));
        docImage.setFitWidth(140);
        docImage.setFitHeight(187);
        docImage.setLayoutX(10);
        docImage.setLayoutY(14);

        ImageView qrImage = new ImageView();
        qrImage.getStyleClass().add("qrcode-icon");
        qrImage.setOnMouseClicked(event -> onQrcode(receipt));
        qrImage.setLayoutX(64);
        qrImage.setLayoutY(225);

        Label label = new Label(doc.getTitle());
        label.setPrefWidth(140);
        label.setAlignment(Pos.CENTER);
        label.setLayoutY(207);
        label.setLayoutX(10);

        pane.getChildren().addAll(docImage, qrImage, label);
        return pane;
    }

    private void onQrcode(Receipt receipt) {
        ReceiptInfo.receipt = receipt;
        Stage qrState = new Stage();
        Parent page = null;
        try {
            URL loginFxmlUrl = getClass().getResource("/fxml/ReceiptInfo.fxml");
            if (loginFxmlUrl != null) {
                page = FXMLLoader.load(loginFxmlUrl);
            }
        } catch (IOException e) {
            throw new RuntimeException("Không tìm thấy đường dẫn của ReceiptInfo.fxml");
        }
        Scene scene = new Scene(page, 533, 600);
        qrState.setResizable(false);
        qrState.getIcons().add(new Image(String.valueOf(getClass().getResource(App.LOGO_PATH))));
        qrState.setTitle("Thông tin phiếu mượn");
        qrState.setScene(scene);
        qrState.show();
    }

    private void displayDetailDoc(Document doc) {
        DocumentInfo.currentDoc = doc;
        try {
            URL url = getClass().getResource("/fxml/DocumentInfo.fxml");
            if (url != null) {
                Parent page = FXMLLoader.load(url);
                docInfoPane.getChildren().clear();
                docInfoPane.getChildren().add(page);
                docInfoPane.setVisible(true);
            }
        } catch (IOException e) {
            throw new RuntimeException("Không tim thấy đường dẫn của DocumentInfo.fxml");
        }
    }
}
