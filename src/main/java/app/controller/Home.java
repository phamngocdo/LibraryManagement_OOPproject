package app.controller;

import app.base.Admin;
import app.base.Author;
import app.base.Category;
import app.base.Document;
import app.run.App;
import app.trie.AuthorNameTrie;
import app.trie.CategoryTrie;
import app.trie.DocumentTitleTrie;
import app.trie.Trie;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Home {

    @FXML
    private Pane functionPane, recommenderPane;

    @FXML
    private ScrollPane searchingResultScrollPane;

    @FXML
    private VBox vBoxInScrollPane;

    @FXML
    private Label resultCount;

    @FXML
    private TextField searchingField;

    @FXML
    private Button removingSearchText;

    @FXML
    private JFXComboBox<String> typeChoosingBox;

    @FXML
    private JFXListView<Pair<String, String>> searchingList;

    @FXML
    private HBox recommenderHBox, topRatingHBox;

    @FXML
    private ProgressIndicator progressIndicator;

    private String typeSearching;
    private Trie trie;

    @FXML
    private void initialize() {
        searchingResultScrollPane.setVisible(false);
        searchingList.setVisible(false);

        setUpComboBox();
        setUpSearchingField();
        setUpRecommendDoc();
    }

    @FXML
    private void onRemoveSearchingText() {
        searchingField.setText("");
    }

    private void setUpRecommendDoc() {
        if (App.currentUser instanceof Admin) {
            progressIndicator.setVisible(false);
            recommenderPane.setVisible(false);
            return;
        }
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                recommenderPane.setVisible(false);
                progressIndicator.setVisible(true);
                ArrayList<Document> topRatingDocs = App.currentUser.seeTopRatingDoc();
                for (int i = 0; i < 5; i++) {
                    //

                    Document doc = topRatingDocs.get(i);
                    Image image = doc.loadImage();
                    int finalI = i;
                    Platform.runLater(() -> {
                        Node paneNode = topRatingHBox.getChildren().get(finalI);
                        paneNode.setOnMouseClicked(event -> displayDetailDoc(doc));
                        if (paneNode instanceof Pane) {
                            for (Node child : ((Pane) paneNode).getChildren()) {
                                if (child instanceof Label) {
                                    ((Label) child).setText(doc.getTitle());
                                } else if (child instanceof ImageView) {
                                    ((ImageView) child).setImage(image);
                                }
                            }
                        }
                    });
                }
                updateMessage("Loading completed");
                progressIndicator.setVisible(false);
                recommenderPane.setVisible(true);
                return null;
            }
        };
        new Thread(task).start();
    }

    private void setUpComboBox() {
        typeSearching = "Tiêu đề";
        trie = new DocumentTitleTrie();
        typeChoosingBox.getItems().addAll("Tiêu đề", "Tác giả", "Thể loại");
        typeChoosingBox.setOnAction(event -> {
            typeSearching = typeChoosingBox.getSelectionModel().getSelectedItem();
            typeChoosingBox.setPromptText(typeSearching);
            new Thread(() -> {
                switch (typeSearching) {
                    case "Tiêu đề":
                        trie = new DocumentTitleTrie();
                        break;
                    case "Tác giả":
                        trie = new AuthorNameTrie();
                        break;
                    case "Thể loại":
                        trie = new CategoryTrie();
                        break;
                }
            }).start();
        });
    }

    private void setUpSearchingField() {
        Platform.runLater(() -> searchingField.getScene().addEventFilter(MouseEvent.MOUSE_PRESSED, event ->{
            if (!searchingField.getBoundsInParent().contains(event.getScreenX(), event.getScreenY())
                && !searchingList.getBoundsInParent().contains(event.getSceneX(), event.getSceneY())
                && !isChildOf(searchingList, (Node) event.getTarget())) {
                searchingList.setVisible(false);
            }
        }));

        searchingField.setOnKeyPressed(event -> {
            if (Objects.requireNonNull(event.getCode()) == KeyCode.ENTER){
                ArrayList<Pair<String, String>> list = new ArrayList<>(searchingList.getItems());
                displayResultFromSearch(list);
            }
        });

        searchingField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean isEmpty = newValue == null || newValue.isEmpty();
            removingSearchText.setVisible(!isEmpty);
            searchingList.setVisible(!isEmpty);
            if (!isEmpty) {
                handleSearchingListView(newValue);
            }
        });
    }

    private boolean isChildOf(Node parent, Node child) {
        while (child != null) {
            if (child.equals(parent)) {
                return true;
            }
            child = child.getParent();
        }
        return false;
    }

    private void handleSearchingListView(String prefix) {
        ObservableList<Pair<String, String>> items = FXCollections.observableArrayList(trie.getAllWordStartWith(prefix));
        searchingList.setItems(items);
        searchingList.setCellFactory(view -> new JFXListCell<>() {
            {
                setOnMouseClicked(event -> {
                    if (event.getClickCount() == 1 && !isEmpty()) {
                        ArrayList<Pair<String, String>> list = new ArrayList<>();
                        list.add(getItem());
                        displayResultFromSearch(list);
                    }
                });
                addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                    if (event.isPrimaryButtonDown()) {
                        searchingList.getSelectionModel().clearSelection();
                        event.consume();
                    }
                });
            }
            @Override
            protected void updateItem(Pair<String, String> item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? null : item.getValue());
            }
        });
        Platform.runLater(() -> {
            int itemCount = searchingList.getItems().size();
            if (itemCount > 0) {
                double totalHeight = itemCount * 33;
                searchingList.setPrefHeight(Math.min(totalHeight, 532));
            }
            else {
                searchingList.setPrefHeight(36);
            }
        });
    }

    private void displayResultFromSearch(ArrayList<Pair<String, String>> resultlist) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                Platform.runLater(() -> {
                    recommenderPane.setVisible(false);
                    searchingList.setVisible(false);
                    searchingResultScrollPane.setVisible(false);
                    progressIndicator.setVisible(true);
                });

                ArrayList<Document> docList = new ArrayList<>();

                if (trie instanceof DocumentTitleTrie) {
                    for (Pair<String, String> pair : resultlist) {
                        docList.add(new Document(pair.getKey()));
                    }
                } else if (trie instanceof AuthorNameTrie) {
                    for (Pair<String, String> pair : resultlist) {
                        Author author = new Author(pair.getKey(), pair.getValue());
                        docList.addAll(author.getAllDoc());
                    }
                } else if (trie instanceof CategoryTrie) {
                    for (Pair<String, String> pair : resultlist) {
                        Category category = new Category(pair.getKey(), pair.getValue());
                        docList.addAll(category.getAllDoc());
                    }
                }

                int listCount = docList.size();
                int hBoxCount = (int) Math.ceil(listCount / 5.0);
                AtomicInteger listIndex = new AtomicInteger(0);
                ArrayList<HBox> hBoxes = new ArrayList<>();
                for (int i = 0; i < hBoxCount; i++) {
                    HBox hBox = new HBox();

                    for (int j = 0; j < 5; j++) {
                        if (listIndex.get() >= listCount) {
                            break;
                        }

                        Document doc = docList.get(listIndex.getAndIncrement());

                        Pane pane = new Pane();
                        pane.setOnMouseClicked(event -> displayDetailDoc(doc));
                        ImageView imageView = new ImageView(doc.loadImage());
                        imageView.setFitWidth(140);
                        imageView.setFitHeight(187);
                        Label label = new Label(doc.getTitle());

                        imageView.setLayoutX(10);
                        imageView.setLayoutY(14);
                        label.setLayoutX(10);
                        label.setLayoutY(201);

                        pane.getChildren().addAll(imageView, label);
                        hBox.getChildren().add(pane);
                    }

                    hBoxes.add(hBox);
                }
                Platform.runLater(() -> {
                    progressIndicator.setVisible(false);
                    resultCount.setText("Số kết quả tìm thấy: " + listCount);
                    vBoxInScrollPane.getChildren().clear();
                    vBoxInScrollPane.getChildren().addAll(hBoxes);
                    searchingResultScrollPane.setVisible(true);
                });

                return null;
            }
        };
        new Thread(task).start();
    }

    private void displayDetailDoc(Document doc) {
        DocumentInfo.currentDoc = doc;
        try {
            URL url = getClass().getResource("/fxml/DocumentInfo.fxml");
            if (url != null) {
                Parent page = FXMLLoader.load(url);
                functionPane.getChildren().clear();
                functionPane.getChildren().add(page);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
