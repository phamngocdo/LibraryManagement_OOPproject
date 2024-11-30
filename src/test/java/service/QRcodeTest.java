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
        HashMap<String, String> data = new HashMap<>();
        data.put("receipt_id", "USHXJWYOKG4N");
        data.put("user_id", "OJIK98JHNTMT");
        data.put("isbn", "QeSnZ4lxNYEC");
        data.put("borrowing_date", "10/06/2024");
        data.put("return_date", "24/06/2024");
        data.put("status", "not returned");

        Image qrCodeImage = QRcode.generateQRCode(data, 300);

        ImageView imageView = new ImageView(qrCodeImage);
        imageView.setFitWidth(300);
        imageView.setFitHeight(300);

        VBox root = new VBox(imageView);
        Scene scene = new Scene(root, 400, 400);

        stage.setTitle("QR Code từ HashMap");
        stage.setScene(scene);
        stage.show();

        System.out.println("In thông tin từ qrcode");
        HashMap<String, String> decodedData = QRcode.decodeQRCode(qrCodeImage);
        System.out.println(decodedData);
    }
}
