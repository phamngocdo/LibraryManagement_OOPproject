package service;

import app.service.QRcode;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;

public class QRcodeTest extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Tạo dữ liệu mẫu cho QR code
        HashMap<String, String> data = new HashMap<>();
        data.put("receipt_id", "USHXJWYOKG4N");
        data.put("user_id", "OJIK98JHNTMT");
        data.put("document_id", "QeSnZ4lxNYEC");
        data.put("borrowing_date", "10/06/2024");
        data.put("return_date", "24/06/2024");
        data.put("status", "not returned");

        // Tạo QR code từ HashMap
        Image qrCodeImage = QRcode.generateQRCode(data, 300);

        // Hiển thị ảnh QR code trong ImageView
        ImageView imageView = new ImageView(qrCodeImage);
        imageView.setFitWidth(300);
        imageView.setFitHeight(300);

        // Thiết lập bố cục và hiển thị giao diện
        VBox root = new VBox(imageView);
        Scene scene = new Scene(root, 400, 400);

        stage.setTitle("QR Code từ HashMap");
        stage.setScene(scene);
        stage.show();

        //in thông tin từ qr code
        System.out.println("In thông tin từ qrcode");
        HashMap<String, String> decodedData = QRcode.decodeQRCode(qrCodeImage);
        System.out.println(decodedData);
    }
}
