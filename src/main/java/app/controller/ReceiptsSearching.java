package app.controller;

import app.base.Admin;
import app.base.Document;
import app.base.Receipt;
import app.run.App;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ReceiptsSearching {

    @FXML
    private TableView<Receipt> receiptsTable;

    @FXML
    private TableColumn<Receipt, String> idReceiptColumn, memberIdColumn, isbnColumn, borrowingDateColumn, dueDateColumn;

    @FXML
    private TableColumn<Receipt, JFXCheckBox> statusColumn;

    @FXML
    private TextField searchTextField;

    @FXML
    private ImageView qrCodeSearch;

    @FXML
    private JFXComboBox<String> filterComboBox;

    private static final String STATUS_RETURNED = "returned";
    private static final String STATUS_NOT_RETURNED = "not returned";
    private final ObservableList<Receipt> receiptsList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        idReceiptColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        memberIdColumn.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        isbnColumn.setCellValueFactory(data -> {
            Receipt receipt = data.getValue();
            String isbn = (new Document(receipt.getDocId())).getIsbn();
            return new javafx.beans.property.SimpleStringProperty(isbn);
        });
        borrowingDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowingDate"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        statusColumn.setCellValueFactory(data -> {
            Receipt receipt = data.getValue();
            JFXCheckBox checkBox = new JFXCheckBox();
            checkBox.setSelected(STATUS_RETURNED.equals(receipt.getStatus()));
            checkBox.setOnAction(event -> confirmReturn(receipt, checkBox.isSelected()));
            checkBox.setStyle("-fx-cursor: HAND");
            return new javafx.beans.property.SimpleObjectProperty<>(checkBox);
        });
        loadReceiptsData();
        receiptsTable.setPlaceholder(new Label("Không tìm thấy thông tin"));

        setupSearchTextField();
        setupFilterComboBox();

        qrCodeSearch.setOnMouseClicked(event -> searchByQRCode());
    }

    private void loadReceiptsData() {
        if (App.currentUser instanceof Admin admin) {
            receiptsList.setAll(admin.seeAllReceipts());
        }
        receiptsTable.setItems(receiptsList);
    }

    private void setupSearchTextField() {
        searchTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String keyword = searchTextField.getText().trim();
                searchReceipts(keyword);
            }
        });
    }

    private void searchReceipts(String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        List<Receipt> filteredReceipts = receiptsList.stream()
                .filter(receipt -> receipt.getId().toLowerCase().startsWith(lowerCaseKeyword) ||
                        receipt.getMemberId().toLowerCase().startsWith(lowerCaseKeyword) ||
                        (new Document(receipt.getDocId())).getIsbn().toLowerCase().startsWith(lowerCaseKeyword))
                .collect(Collectors.toList());
        receiptsTable.setItems(FXCollections.observableArrayList(filteredReceipts));
    }

    private void setupFilterComboBox() {
        filterComboBox.setItems(FXCollections.observableArrayList("Tất cả", "Đã trả", "Chưa trả", "Quá hạn"));
        filterComboBox.setOnAction(event -> {
            String selectedFilter = filterComboBox.getValue();
            filterComboBox.setPromptText(selectedFilter);
            List<Receipt> filteredReceipts = receiptsList.stream()
                    .filter(receipt -> switch (selectedFilter) {
                        case "Đã trả" -> STATUS_RETURNED.equals(receipt.getStatus());
                        case "Chưa trả" -> STATUS_NOT_RETURNED.equals(receipt.getStatus());
                        case "Quá hạn" -> isOverdue(receipt.getDueDate())
                                && STATUS_NOT_RETURNED.equals(receipt.getStatus());
                        default -> true;
                    })
                    .collect(Collectors.toList());
            receiptsTable.setItems(FXCollections.observableArrayList(filteredReceipts));
        });
    }

    private boolean isOverdue(String dueDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date due = dateFormat.parse(dueDate);
            Date currentDate = new Date();
            return due.before(currentDate);
        } catch (ParseException e) {
            throw new RuntimeException("Định dạng ngày không phải DD-MM-YYYY");
        }
    }

    private void searchByQRCode() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn hình ảnh QR Code");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Image Files", "*.png", "*.jpg", "*.jpeg"
                ));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            JSONObject decodedQR = decodeQRCode(selectedFile);
            for (Receipt receipt : receiptsList) {
                if (receipt.getId().equals(decodedQR.getString("receiptId"))
                        && receipt.getStatus().equals(decodedQR.getString("status"))) {
                    receiptsTable.getItems().clear();
                    receiptsTable.getItems().add(receipt);
                    break;
                }
            }
        }
    }

    private JSONObject decodeQRCode(File file) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            BinaryBitmap binaryBitmap = new BinaryBitmap(
                    new HybridBinarizer(
                            new BufferedImageLuminanceSource(bufferedImage)));

            Result result = new MultiFormatReader().decode(binaryBitmap);
            String qrCodeData = result.getText();
            return new JSONObject(qrCodeData);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi giải mã QR code từ file: " + file.getName(), e);
        }
    }

    private void confirmReturn(Receipt receipt, boolean isReturned) {
        if (App.currentUser instanceof Admin admin) {
            admin.confirmReturnDocument(receipt, isReturned);
            receiptsTable.refresh();
        }
    }
}
