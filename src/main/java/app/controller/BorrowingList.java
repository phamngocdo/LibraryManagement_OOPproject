package app.controller;

import app.base.Document;
import app.base.Member;
import app.base.Receipt;
import app.run.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
    private Pane docInfoPane;

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
        addDocToVBox(returnedList, returnedDocVBox);
        addDocToVBox(notReturnedList, notReturnedDocVBox);
    }

    private void addDocToVBox(ArrayList<Receipt> list, VBox vBox) {
        int hBoxCount = (int) Math.ceil((double) list.size() / 5);
        int listIndex = 0;
        for (int i = 0; i < hBoxCount; i++) {
            HBox hBox = new HBox();
            for (int j = 0; j < 5; j++) {
                if (listIndex == list.size()) {
                    break;
                }
                Receipt receipt = list.get(listIndex++);
                Document doc = new Document(receipt.getDocId());

                Pane pane = new Pane();

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
                qrImage.setLayoutY(226);

                Label title = new Label(doc.getTitle());
                title.setLayoutX(10);
                title.setLayoutY(201);

                pane.getChildren().addAll(docImage, qrImage, title);

                hBox.getChildren().add(pane);
            }
            vBox.getChildren().add(hBox);
        }
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
    }
}
