package app.controller;

import app.base.Receipt;
import app.service.QRcode;
import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ReceiptInfo {

    @FXML
    private Pane infoPane;

    @FXML
    private Label receiptIdLabel, docIdLabel, memberIdLabel, borrowingDateLabel, dueDateLabel, statusLabel;

    @FXML
    private JFXTextArea fixation;

    @FXML
    private TextFlow signature;

    @FXML
    private ImageView qrCodeImageView, printerImageView, infoSavingImageView;

    public static Receipt receipt;

    @FXML
    private void initialize() {
        if (receipt != null) {
            receiptIdLabel.setText(receipt.getId());
            docIdLabel.setText(receipt.getDocId());
            memberIdLabel.setText(receipt.getMemberId());
            borrowingDateLabel.setText(receipt.getBorrowingDate());
            dueDateLabel.setText(receipt.getDueDate());
            statusLabel.setText(receipt.getStatus().equals("returned") ? "Đã trả" : "Chưa trả");
            try {
                qrGenerate();
            } catch (Exception e) {
                throw new RuntimeException("Tạo QR thất bại");
            }
        }
        setUpFixation();
        signature.setVisible(false);
        qrCodeImageView.setVisible(true);
        printerImageView.setVisible(true);
    }

    private void setUpFixation() {
        String s = "1. Người mụơn phải trả sách đúng hạn." + "\n" +
                "2. Người mượn phải giữ gìn sách nguyên vẹn." + "\n" +
                "3. Nếu trả sách không đúng hạn hoặc hư hỏng thì phải bồi thường theo quy định." + "\n" +
                "4. Khi trả hoặc mượn sách phải mang phiếu và sách đến quầy để xác nhận.";
        fixation.setText(s);
    }

    private void qrGenerate() throws Exception {
        HashMap<String, String> map = new HashMap<>();
        map.put("receiptId", receipt.getId());
        map.put("docId", docIdLabel.getText());
        map.put("memberId", memberIdLabel.getText());
        map.put("borrowingDate", borrowingDateLabel.getText());
        map.put("dueDate", dueDateLabel.getText());
        map.put("status", statusLabel.getText());
        qrCodeImageView.setImage(QRcode.generateQRCode(map, 200));
    }

    @FXML
    private void onPrinterImageView(MouseEvent event) {
        printerImageView.setVisible(false);
        infoSavingImageView.setVisible(false);
        signature.setVisible(true);

        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null) {
            boolean proceed = printerJob.showPrintDialog(infoPane.getScene().getWindow());
            if (proceed) {
                boolean success = printerJob.printPage(infoPane);
                if (success) {
                    printerJob.endJob();
                    System.out.println("In thành công!");
                } else {
                    System.out.println("In thất bại.");
                }
            }
        } else {
            System.out.println("Không tìm thấy máy in.");
        }
        printerImageView.setVisible(true);
        infoSavingImageView.setVisible(true);
        signature.setVisible(false);
    }

    @FXML
    private void onSaveInfo(MouseEvent event) {
        printerImageView.setVisible(false);
        infoSavingImageView.setVisible(false);
        signature.setVisible(true);

        WritableImage snapshot = new WritableImage((int) infoPane.getWidth(), (int) infoPane.getHeight());
        infoPane.snapshot(null, snapshot);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lưu phiếu mượn");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG File","*.png"));

        File file = fileChooser.showSaveDialog(infoPane.getScene().getWindow());
        if (file != null) {
            try {
                BufferedImage bufferedImage = new BufferedImage((int) snapshot.getWidth(), 
                                                                (int) snapshot.getHeight(),
                                                                BufferedImage.TYPE_INT_ARGB);
                PixelReader pixelReader = snapshot.getPixelReader();
                for (int y = 0; y < snapshot.getHeight(); y++) {
                    for (int x = 0; x < snapshot.getWidth(); x++) {
                        bufferedImage.setRGB(x, y, pixelReader.getArgb(x, y));
                    }
                }
                ImageIO.write(bufferedImage, "png", file);
                System.out.println("Lưu thành công vào " + file.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("Lưu thất bại");
            }
        }
        setUpFixation();
        printerImageView.setVisible(true);
        infoSavingImageView.setVisible(true);
        signature.setVisible(false);
    }
}
