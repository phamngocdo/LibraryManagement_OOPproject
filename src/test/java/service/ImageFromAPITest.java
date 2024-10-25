package service;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ImageFromAPITest extends Application {
    @Override
    public void start(Stage primaryStage) {
        String imageUrl = "http://books.google.com/books/content?id=j2g5AAAAMAAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"; // Thay đổi URL với địa chỉ thực tế
        int imageWidth = 400; // Chiều rộng bạn muốn cho ảnh

        // Gọi hàm getImageFromURL để lấy ImageView
        ImageView imageView = ImageFromAPI.getImageFromURL(imageUrl, imageWidth);

        // Tạo Pane và thêm ImageView
        Pane pane = new Pane();
        pane.getChildren().add(imageView);

        // Thiết lập Scene và Stage
        Scene scene = new Scene(pane, 800, 600); // Đặt kích thước cho cửa sổ
        primaryStage.setTitle("Hiển Thị Ảnh Từ URL");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
