package app.service;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageFromAPI {

    public static ImageView getImageFromURL(String url, int width) {
        //Hàm này trả về ảnh từ link trên gg book API, nên xem trên database api nó như nàê
        Image image = new Image(url, true); // 'true' để tải ảnh không đồng bộ
        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(width);
        imageView.setPreserveRatio(true);  // Giữ nguyên tỉ lệ ảnh

        return imageView;
    }
}
