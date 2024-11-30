package app.controller;

import app.base.Member;
import app.run.App;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    private JFXButton editingButton, cancelPasswordButton, changePasswordButton, saveButton;

    private boolean isEditing = false;

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
        idMenberTextField.setEditable(false);
        loginNameTextField.setEditable(false);
        firstNameTextField.setEditable(false);
        lastNameTextField.setEditable(false);
        birthdayTextField.setEditable(false);
        emailTextField.setEditable(false);
        phoneNumberTextFiled.setEditable(false);

        editingButton.setVisible(true);
        saveButton.setVisible(false);
        changePasswordButton.setVisible(false);
        cancelPasswordButton.setVisible(false);
        passwordVBox.setVisible(false);

        isEditing = false;
    }

    @FXML
    private void editInfo() {
        if (isEditing) {
            setupPersonalInfo();
        } else {
            firstNameTextField.setEditable(true);
            lastNameTextField.setEditable(true);
            birthdayTextField.setEditable(true);
            emailTextField.setEditable(true);
            phoneNumberTextFiled.setEditable(true);

            editingButton.setVisible(true);
            saveButton.setVisible(true);
            changePasswordButton.setVisible(true);
            resultLabel.setVisible(false);

            isEditing = true;
        }
        editingButton.setVisible(false);
    }

    @FXML
    private void changePassword() {
        changePasswordButton.setVisible(false);
        cancelPasswordButton.setVisible(true);
        passwordVBox.setVisible(true);
    }

    @FXML
    private void cancelPassword() {
        changePasswordButton.setVisible(true);
        cancelPasswordButton.setVisible(false);
        passwordVBox.setVisible(false);
    }

    @FXML
    private void saveInfo() {
        resultLabel.getStyleClass().clear();
        resultLabel.getStyleClass().add("er-result-label");
        try {
            if (firstNameTextField.getText().isEmpty() || lastNameTextField.getText().isEmpty() ||
                    birthdayTextField.getText().isEmpty() || emailTextField.getText().isEmpty() ||
                    phoneNumberTextFiled.getText().isEmpty()) {
                resultLabel.setText("Vui lòng điền đầy đủ thông tin!");
                resultLabel.setVisible(true);
                return;
            }

            String emailPattern = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
            if (!Pattern.matches(emailPattern, emailTextField.getText())) {
                resultLabel.setText("Email không hợp lệ!");
                resultLabel.setVisible(true);
                return;
            }

            if (!phoneNumberTextFiled.getText().matches("\\d+")) {
                resultLabel.setText("Số điện thoại chỉ chứa chữ số!");
                resultLabel.setVisible(true);
                return;
            }

            String datePattern = "^\\d{2}-\\d{2}-\\d{4}$";
            if (!Pattern.matches(datePattern, birthdayTextField.getText())) {
                resultLabel.setText("Ngày sinh không đúng định dạng DD-MM-YYYY!");
                resultLabel.setVisible(true);
                return;
            }

            if (App.currentUser instanceof Member) {
                App.currentUser.setId(idMenberTextField.getText());
                App.currentUser.setUsername(loginNameTextField.getText());
                App.currentUser.setFirstName(firstNameTextField.getText());
                App.currentUser.setLastName(lastNameTextField.getText());
                App.currentUser.setBirthday(birthdayTextField.getText());
                App.currentUser.setEmail(emailTextField.getText());
                App.currentUser.setPhoneNumber(phoneNumberTextFiled.getText());
            }

            if (passwordVBox.isVisible()) {
                String oldPassword = oldPasswordTextField.getText();
                String newPassword = newPasswordTextField.getText();
                String reEnterNewPassword = reEnterNewPasswordTextField.getText();

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

                String currentPassword = App.currentUser.getPassword();
                if (currentPassword == null || !currentPassword.equals(oldPassword)) {
                    resultLabel.setText("Mật khẩu cũ không chính xác!");
                    resultLabel.setVisible(true);
                    return;
                }
                App.currentUser.setPassword(newPassword);
            }

            Member.updateInfoToDatabase((Member) App.currentUser);
            resultLabel.getStyleClass().clear();
            resultLabel.getStyleClass().add("suc-result-label");
            resultLabel.setText("Chỉnh sửa thành công!");
            resultLabel.setVisible(true);

            setupPersonalInfo();

        } catch (Exception e) {
            resultLabel.setText("Lỗi khi lưu thông tin!");
            resultLabel.setVisible(true);
            throw new RuntimeException(e);
        }
    }

}
