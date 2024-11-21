package app.controller;

import app.base.Admin;
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
    private TableColumn<Receipt, String> idReceiptColumn, memberIdColumn, docIdColumn, borrowingDateColumn, dueDateColumn;

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
        // Gán các cột với các thuộc tính của Receipt
        idReceiptColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        memberIdColumn.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        docIdColumn.setCellValueFactory(new PropertyValueFactory<>("docId"));
        borrowingDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowingDate"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        statusColumn.setCellValueFactory(data -> {
            Receipt receipt = data.getValue();
            JFXCheckBox checkBox = new JFXCheckBox();
            checkBox.setSelected(STATUS_RETURNED.equals(receipt.getStatus()));
            checkBox.setOnAction(event -> confirmReturn(receipt, checkBox.isSelected()));
            return new javafx.beans.property.SimpleObjectProperty<>(checkBox);
        });

        //loadreceipt vào bảng
        loadReceiptsData();
        //Hiển thị thông báo khi không có dữ liệu
        receiptsTable.setPlaceholder(new Label("Không tìm thấy thông tin"));

        //tìm kiếm ký tự bắt đầu
        setupSearchTextField();

        //lọc receipt
        setupFilterComboBox();

        // Xử lý tìm kiếm QR code
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
                String keyword = searchTextField.getText().trim(); //lấy từ khóa từ TextField
                searchReceipts(keyword); //gọi searchReceipts
            }
        });
    }

    private void searchReceipts(String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        List<Receipt> filteredReceipts = receiptsList.stream()
                .filter(receipt -> receipt.getId().toLowerCase().startsWith(lowerCaseKeyword) ||
                        receipt.getMemberId().toLowerCase().startsWith(lowerCaseKeyword) ||
                        receipt.getDocId().toLowerCase().startsWith(lowerCaseKeyword))
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
                        default -> true; // "Tất cả"
                    })
                    .collect(Collectors.toList());
            receiptsTable.setItems(FXCollections.observableArrayList(filteredReceipts));
        });
    }

    // Hàm kiểm tra xem phiếu mượn có quá hạn không
    private boolean isOverdue(String dueDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date due = dateFormat.parse(dueDate);
            Date currentDate = new Date();

            // So sánh ngày hiện tại với ngày hết hạn
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
            // Lấy đối tượng Receipt sau khi giải mã QR code
            Receipt decodedReceipt = decodeQRCode(selectedFile);
            // So sánh đối tượng receipt với danh sách receiptsList
            List<Receipt> matchedReceipts = receiptsList.stream()
                    .filter(receipt -> receipt.equals(decodedReceipt))
                    .collect(Collectors.toList());

            // Hiển thị kết quả tìm kiếm
            receiptsTable.setItems(FXCollections.observableArrayList(matchedReceipts));
        }
    }

    private Receipt decodeQRCode(File file) {
        try {
            // Đọc file ảnh vào BufferedImage
            BufferedImage bufferedImage = ImageIO.read(file);

            // Chuyển thành BinaryBitmap
            BinaryBitmap binaryBitmap = new BinaryBitmap(
                    new HybridBinarizer(
                            new BufferedImageLuminanceSource(bufferedImage)));

            Result result = new MultiFormatReader().decode(binaryBitmap);
            // Lấy JSON sau khi giải mã QR
            String qrCodeData = result.getText();
            JSONObject jsonObject = new JSONObject(qrCodeData);
            // Tạo đối tượng Receipt từ dữ liệu JSON
            return new Receipt(
                    jsonObject.getString("receiptId"),
                    jsonObject.getString("memberId"),
                    jsonObject.getString("docId"),
                    jsonObject.getString("borrowingDate"),
                    jsonObject.getString("dueDate"),
                    jsonObject.getString("status")
            );
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
