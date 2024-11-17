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
    // Class này hiển thị tất cả các phiếu mượn cho admin xem (sử dụng (admin) app.currentUser.seeAllReceipts)
    // Một số các chức năng cần viết:

    // Hiển thị các thông tin của từng phiếu vào bảng tableView,
    //              nếu không có thì hiển thị không tìm thấy thông tin ở giữa bằng cách nào đó (có thể cho vào bảng càng tốt)
    // Trong bảng này các cột từ 1 đến 5 là kiểu String, riêng cột 6 là JFXCheckBox (để text là"") sử dụng để Admin đánh dấu là đã trả sách
    //                                                                              (nhớ sử dụng (admin) app.currentUser.confirmreturnDoc)


    // Xử lí thanh tìm kiếm: Khi người dùng nhập một chuỗi thì hiển thị phiếu có id phiếu hoặc id doc hoặc id member
    //                       bắt đầu bằng chuỗi đấy

    // Lọc thông tin phiếu: Sử dụng JFXComboBox để chọn thông tin cần lọc: Tất cả (mặc định), Đã trả, Chưa trả, Quá hạn

    // Tìm kiếm thông tin bằng QR: nhúng event mouseclick cho cái ảnh QRcode, sử dụng FileChooser để chọn ảnh và hiển thị
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
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> searchReceipts(newValue));

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

    private void searchReceipts(String keyword) {
        List<Receipt> filteredReceipts = receiptsList.stream()
                .filter(receipt -> receipt.getId().startsWith(keyword) ||
                        receipt.getMemberId().startsWith(keyword) ||
                        receipt.getDocId().startsWith(keyword))
                .collect(Collectors.toList());
        receiptsTable.setItems(FXCollections.observableArrayList(filteredReceipts));
    }

    private void setupFilterComboBox() {
        filterComboBox.setItems(FXCollections.observableArrayList("Tất cả", "Đã trả", "Chưa trả", "Quá hạn"));
        filterComboBox.setOnAction(event -> {
            String selectedFilter = filterComboBox.getValue();
            List<Receipt> filteredReceipts = receiptsList.stream()
                    .filter(receipt -> {
                        return switch (selectedFilter) {
                            case "Đã trả" -> STATUS_RETURNED.equals(receipt.getStatus());
                            case "Chưa trả" -> STATUS_NOT_RETURNED.equals(receipt.getStatus());
                            case "Quá hạn" -> isOverdue(receipt.getDueDate())
                                    && STATUS_NOT_RETURNED.equals(receipt.getStatus());
                            default -> true; // "Tất cả"
                        };
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
            throw new RuntimeException(e);
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
            //lấy docId sau khi giải mã qr
            String qrCodeData = decodeQRCode(selectedFile);
            if (qrCodeData != null) {
                searchReceipts(qrCodeData);
            }
        }
    }

    private String decodeQRCode(File file) {
        try {
            // Đọc file ảnh vào BufferedImage
            BufferedImage bufferedImage = ImageIO.read(file);
            //chuyển thành BinaryBitmap
            BinaryBitmap binaryBitmap = new BinaryBitmap(
                    new HybridBinarizer(
                            new BufferedImageLuminanceSource(bufferedImage)));

            Result result = new MultiFormatReader().decode(binaryBitmap);
            //lấy hashmap sau khi giải mã qr
            String qrCodeData = result.getText();

            JSONObject jsonObject = new JSONObject(qrCodeData);
            // Trả về giá trị docId
            return jsonObject.getString("docId");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void confirmReturn(Receipt receipt, boolean isReturned) {
        if (App.currentUser instanceof Admin admin) {
            admin.confirmReturnDocument(receipt, isReturned);
            receiptsTable.refresh();
        }
    }
}
