package app.controller;

import app.base.Admin;
import app.base.Document;
import app.run.App;
import app.service.GoogleBookAPI;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;


public class DocumentEditing {

    //Điền các @FXML ở đây
    @FXML
    private Label functionLabel, resultLabel;

    private Document doc;

    @FXML
    void initialize() {
        if (doc != null) { // Nếu doc khác null tức là đang chỉnh sửa
            functionLabel.setText("Chỉnh sửa thông tin tài liệu");
            //set up các thông tin của doc vào các textfield
        } else {
            functionLabel.setText("Thêm tài liệu");
        }
        //Set visible cho các cái cần thiết
    }

    @FXML
    private void onSaveDocument() {
        if (doc != null) {
            resultLabel.setText(((Admin) App.currentUser).updateDocument(doc));
        } else {
            // Lấy dữ liệu từ các textField và tạo new Document()
            resultLabel.setText(((Admin) App.currentUser).addDocument(doc));
        }

    }

    @FXML
    private void onDeleteDocument() {
        if (doc != null) {
            resultLabel.setText(((Admin) App.currentUser).removeDocument(doc.getId()));
        }
    }

    @FXML
    private void onCancel() {
        if (doc != null) {
            //Nút này giống reset về ban đầu
            //set up các thông tin của doc vào các textfield
        } else {
            //Xóa các thông tin trong textfield
        }
    }

    @FXML
    private void onGetAPIInfo() throws IOException {
        /**
         * Nếu id không có thông tin thì resultLabel.setText("Vui lòng nhập id")
         * Nếu thông tin từ API rỗng thì in ra resultLabel.setText("Không có kết quả tương ứng")
         * Sử dụng đa luồng cho phần này, khi kết quả đang chờ thì cho hiện cái progressIndicator, khi có kết quả thì ẩn
         */
    }

    public void setDocument(Document doc) {
        this.doc = doc;
    }
}
