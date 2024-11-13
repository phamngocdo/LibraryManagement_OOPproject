package app.controller;

import app.base.Member;
import app.dao.UserDAO;
import app.run.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.regex.Pattern;

public class MemberInfo {
    @FXML
    private Label resultLabel;

    @FXML
    private VBox passwordVBox;

    @FXML
    private TextField idMenberTextField, loginNameTextField, firstNameTextField, lastNameTextField;

    @FXML
    private TextField birthdayTextField, emailTextField, phoneNumberTextFiled;

    @FXML
    private TextField oldPasswordTextField, newPasswordTextField, reEnterNewPasswordTextField;

    @FXML
    private Button editingButton, cancelPasswordButton, changePasswordButton, saveButton;

    @FXML
    private HBox personalInfoHBox;

    private boolean isEditing = false; // Biến để kiểm tra trạng thái chỉnh sửa

    @FXML
    private void initialize() {
        setupPersonalInfo();
    }

    private void setupPersonalInfo() {
        Member currentMember = (Member) App.currentUser;
        idMenberTextField.setText(currentMember.getId());
        loginNameTextField.setText(currentMember.getUsername());
        firstNameTextField.setText(currentMember.getFirstName());
        lastNameTextField.setText(currentMember.getLastName());
        birthdayTextField.setText(currentMember.getBirthday());
        emailTextField.setText(currentMember.getEmail());
        phoneNumberTextFiled.setText(currentMember.getPhoneNumber());
        //Hiển thị bảng thông tin cá nhân và nút chỉnh sửa
        personalInfoHBox.setVisible(true);
        idMenberTextField.setDisable(true);
        loginNameTextField.setDisable(true);
        firstNameTextField.setDisable(true);
        lastNameTextField.setDisable(true);
        birthdayTextField.setDisable(true);
        emailTextField.setDisable(true);
        phoneNumberTextFiled.setDisable(true);

        editingButton.setVisible(true);
        saveButton.setVisible(false);
        changePasswordButton.setVisible(false);
        cancelPasswordButton.setVisible(false);
        passwordVBox.setVisible(false);  // Ẩn phần thay đổi mật khẩu

        isEditing = false;
    }

    @FXML
    private void editInfo() {
        /**
         * Khi nhấn nút chỉnh sửa thì hiển thị nút thay đổi mật khẩu và nút lưu
         */
        if (isEditing) {
            // Nếu đang ở chế độ chỉnh sửa, quay lại trạng thái ban đầu
            setupPersonalInfo();
        } else {
            // Nếu không ở chế độ chỉnh sửa, chuyển sang chế độ chỉnh sửa
            firstNameTextField.setDisable(false);
            lastNameTextField.setDisable(false);
            birthdayTextField.setDisable(false);
            emailTextField.setDisable(false);
            phoneNumberTextFiled.setDisable(false);

            editingButton.setVisible(true);
            saveButton.setVisible(true);
            changePasswordButton.setVisible(true);
            resultLabel.setVisible(false);

            isEditing = true; // Đặt trạng thái thành đang chỉnh sửa
        }
    }

    @FXML
    private void changePassword() {
        /**
         * Khi nhấn nút thay đổi mật khẩu thì hiển thị nút hủy thay đổi mật khẩu và passwordVBox
         */
        changePasswordButton.setVisible(false);
        cancelPasswordButton.setVisible(true);
        passwordVBox.setVisible(true);
    }

    @FXML
    private void cancelPassword() {
        /**
         * Khi nhấn nút hủy thay đổi mật khẩu thì hiển thị nút thay đổi mật khẩu và bỏ passwordVBox
         */
        changePasswordButton.setVisible(true);
        cancelPasswordButton.setVisible(false);
        passwordVBox.setVisible(false);
    }

    @FXML
    private void saveInfo() {
        /**
         * Khi nhấn nút lưu thì đổi mật khẩu cũ thành mật khẩu mới và
         * resultLabel hiển thị chỉnh sửa thành công
         */
        try {
            // Kiểm tra các trường không được để trống
            if (firstNameTextField.getText().isEmpty() || lastNameTextField.getText().isEmpty() ||
                    birthdayTextField.getText().isEmpty() || emailTextField.getText().isEmpty() ||
                    phoneNumberTextFiled.getText().isEmpty()) {
                resultLabel.setText("Vui lòng điền đầy đủ thông tin!");
                resultLabel.setVisible(true);
                return;
            }
            
            // Kiểm tra định dạng email hợp lệ
            String emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
            if (!Pattern.matches(emailPattern, emailTextField.getText())) {
                resultLabel.setText("Email không hợp lệ!");
                resultLabel.setVisible(true);
                return;
            }

            // Kiểm tra số điện thoại có phải là số không
            if (!phoneNumberTextFiled.getText().matches("\\d+")) {
                resultLabel.setText("Số điện thoại chỉ chứa chữ số!");
                resultLabel.setVisible(true);
                return;
            }

            // Kiểm tra định dạng ngày sinh là DD-MM-YYYY
            String datePattern = "^\\d{2}-\\d{2}-\\d{4}$";
            if (!Pattern.matches(datePattern, birthdayTextField.getText())) {
                resultLabel.setText("Ngày sinh không đúng định dạng DD-MM-YYYY!");
                resultLabel.setVisible(true);
                return;
            }
            // Lấy thông tin từ các TextField và tạo đối tượng Member
            if (App.currentUser instanceof Member) {
                App.currentUser.setId(idMenberTextField.getText());
                App.currentUser.setUsername(loginNameTextField.getText());
                App.currentUser.setFirstName(firstNameTextField.getText());
                App.currentUser.setLastName(lastNameTextField.getText());
                App.currentUser.setBirthday(birthdayTextField.getText());
                App.currentUser.setEmail(emailTextField.getText());
                App.currentUser.setPhoneNumber(phoneNumberTextFiled.getText());
            }

            // Nếu passwordVBox đang hiển thị, tức là người dùng muốn thay đổi mật khẩu
            if (passwordVBox.isVisible()) {
                String oldPassword = oldPasswordTextField.getText();
                String newPassword = newPasswordTextField.getText();
                String reEnterNewPassword = reEnterNewPasswordTextField.getText();

                // Kiểm tra tính hợp lệ của mật khẩu mới
                if (oldPassword.isEmpty() || newPassword.isEmpty() || reEnterNewPassword.isEmpty()) {
                    resultLabel.setText("Vui lòng nhập đầy đủ mật khẩu cũ và mới!");
                    resultLabel.setVisible(true);
                    return;
                }

                if (!newPassword.equals(reEnterNewPassword)) {
                    resultLabel.setText("Mật khẩu mới không khớp!");
                    resultLabel.setVisible(true);
                    return;
                }

                // Kiểm tra mật khẩu cũ có chính xác không
                String currentPassword = App.currentUser.getPassword();
                if (currentPassword == null || !currentPassword.equals(oldPassword)) {
                    resultLabel.setText("Mật khẩu cũ không chính xác!");
                    resultLabel.setVisible(true);
                    return;
                }
                App.currentUser.setPassword(newPassword); // Đặt mật khẩu mới
            }

            // Gọi hàm updateMember để lưu thông tin mới vào cơ sở dữ liệu
            Member.updateInfoToDatabase((Member) App.currentUser);
            resultLabel.setText("Chỉnh sửa thành công!");
            resultLabel.setVisible(true);

            // Quay lại trạng thái ban đầu sau khi lưu thành công
            setupPersonalInfo();

        } catch (Exception e) {
            resultLabel.setText("Lỗi khi lưu thông tin!");
            resultLabel.setVisible(true);
            throw new RuntimeException(e);
        }
    }

}
