package app.service;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class QRcode {

    // Tạo QR code từ HashMap thông tin phiếu mượn, trả về Image
    public static Image generateQRCode(HashMap<String, String> data, int size) throws Exception {
        // Chuyển HashMap thành chuỗi JSON
        StringBuilder json = new StringBuilder("{");
        for (Map.Entry<String, String> entry : data.entrySet()) {
            json.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\",");
        }
        json.deleteCharAt(json.length() - 1).append("}");

        // Thiết lập thông số QR code
        Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        BitMatrix bitMatrix = new MultiFormatWriter().encode(
                json.toString(), BarcodeFormat.QR_CODE, size, size, hints);

        // Lưu QR code vào ảnh BufferedImage
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        // Chuyển BufferedImage thành Image (JavaFX)
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        return new Image(inputStream);
    }

    // Giải mã QR code từ Image và trả về HashMap
    public static HashMap<String, String> decodeQRCode(Image qrImage) throws Exception {
        // Chuyển Image (JavaFX) thành BufferedImage
        BufferedImage bufferedImage = convertToBufferedImage(qrImage);

        BinaryBitmap binaryBitmap = new BinaryBitmap(
                new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));

        // Giải mã QR code
        Result result = new MultiFormatReader().decode(binaryBitmap);

        // Chuyển chuỗi JSON trở lại HashMap
        String json = result.getText();
        HashMap<String, String> data = new HashMap<>();
        String[] entries = json.replace("{", "").replace("}", "").split(",");
        for (String entry : entries) {
            String[] keyValue = entry.split(":");
            data.put(keyValue[0].replace("\"", ""), keyValue[1].replace("\"", ""));
        }
        return data;
    }

    // Hàm hỗ trợ chuyển JavaFX Image thành BufferedImage
    private static BufferedImage convertToBufferedImage(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Đọc pixel từ JavaFX Image và ghi vào BufferedImage
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = image.getPixelReader().getArgb(x, y);
                bufferedImage.setRGB(x, y, argb);
            }
        }
        return bufferedImage;
    }

}
