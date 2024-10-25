package app.controller;

import app.base.Admin;
import app.base.Member;
import app.base.User;
import app.run.App;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;

public class Login {

    //Login Pane
    @FXML
    private Pane loginPane;

    @FXML
    private TextField usernameLoginTextField, notHiddenPasswordLoginField;

    @FXML
    private PasswordField hiddenPasswordLoginField;

    @FXML
    private RadioButton passwordDisplayLogin;

    @FXML
    private Label loginResult;

    @FXML
    private JFXButton loginButton;

    @FXML
    private Hyperlink memberRegisterLink;

    //Register Pane
    @FXML
    private Pane registerPane;

    @FXML
    private TextField firstNameTextField, lastNameTextField, emailTextField, phoneNumberTextField, birthdayTextField;

    @FXML
    private TextField usernameRegisterTextField, notHiddenPasswordRegisterField, notHiddenConfirmPasswordField;

    @FXML
    private PasswordField hiddenPasswordRegisterField, hiddenConfirmPasswordField;

    @FXML
    private RadioButton passwordDisplayRegister;

    @FXML
    private JFXButton registerButton;

    @FXML
    private Label registerResult;

    @FXML
    private Hyperlink loginLink;

    @FXML
    private void initialize() {
        loginPane.setVisible(true);
        hiddenPasswordLoginField.setVisible(true);
        notHiddenConfirmPasswordField.setVisible(false);

        registerPane.setVisible(false);
        hiddenPasswordRegisterField.setVisible(true);
        hiddenConfirmPasswordField.setVisible(true);
        notHiddenPasswordRegisterField.setVisible(false);
        notHiddenConfirmPasswordField.setVisible(false);

        passwordDisplayLogin.selectedProperty().addListener((observable, oldValue, newValue) ->
            setDisplayPasswordLogin(newValue)
        );

        passwordDisplayRegister.selectedProperty().addListener((observable, oldValue, newValue) ->
            setDisplayPasswordRegister(newValue)
        );

        loginResult.setText("");
        registerResult.setText("");
    }

    @FXML
    private void onLoginButton() {
        String username = usernameLoginTextField.getText();
        String password;
        if (passwordDisplayLogin.isSelected()) {
            password = notHiddenPasswordLoginField.getText();
        } else {
            password = hiddenPasswordLoginField.getText();
        }

        if (password.isEmpty() || username.isEmpty()) {
            loginResult.setText("Bạn chưa điền đầy đủ thông tin!");
            return;
        }

        if ((App.currentUser = User.login(usernameLoginTextField.getText(), password)) == null) {
            loginResult.setText("Sai tên đăng nhập hoặc mật khẩu!");
            return;
        }

        goToMainScreen();
    }

    @FXML
    private void onRegisterButton() {
        String username = usernameRegisterTextField.getText();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String email = emailTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String birthday = birthdayTextField.getText();
        String password;
        String confirmPassword;
        if (passwordDisplayRegister.isSelected()) {
            password = notHiddenPasswordRegisterField.getText();
            confirmPassword = notHiddenConfirmPasswordField.getText();
        } else {
            password = hiddenPasswordRegisterField.getText();
            confirmPassword = hiddenConfirmPasswordField.getText();
        }

        registerResult.getStyleClass().clear();
        registerResult.getStyleClass().add("er-result-label");

        if (username.isEmpty()
                || firstName.isEmpty()
                || lastName.isEmpty()
                || birthday.isEmpty()
                || email.isEmpty()
                || phoneNumber.isEmpty()
                || password.isEmpty()
                || confirmPassword.isEmpty()) {
            registerResult.setText("Bạn cần điển đầy đủ thông tin!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            registerResult.setText("Mật khẩu không trùng khớp!");
            return;
        }

        Member newMember = new Member("", username, password, firstName, lastName, birthday, email, phoneNumber);
        registerResult.setText(newMember.register());
        if (registerResult.getText().equals("Đăng nhập thành công")) {
            registerResult.getStyleClass().clear();
            registerResult.getStyleClass().add("suc-result-label");
        }
    }

    @FXML void onLoginLink() {
        loginPane.setVisible(true);
        registerPane.setVisible(false);
    }

    @FXML void onMemberRegisterLink() {
        loginPane.setVisible(false);
        registerPane.setVisible(true);
    }

    private void setDisplayPasswordLogin(boolean isSelected) {
        if (isSelected) {
            notHiddenPasswordLoginField.setText(hiddenPasswordLoginField.getText());
            notHiddenPasswordLoginField.setVisible(true);
            hiddenPasswordLoginField.setVisible(false);
        } else {
            hiddenPasswordLoginField.setText(notHiddenPasswordLoginField.getText());
            notHiddenPasswordLoginField.setVisible(false);
            hiddenPasswordLoginField.setVisible(true);
        }
    }

    private void setDisplayPasswordRegister(boolean isSelected) {
        if (isSelected) {
            notHiddenPasswordRegisterField.setText(hiddenPasswordRegisterField.getText());
            notHiddenPasswordRegisterField.setVisible(true);
            hiddenPasswordRegisterField.setVisible(false);

            notHiddenConfirmPasswordField.setText(hiddenConfirmPasswordField.getText());
            notHiddenConfirmPasswordField.setVisible(true);
            hiddenConfirmPasswordField.setVisible(false);
        } else {
            hiddenPasswordRegisterField.setText(notHiddenPasswordRegisterField.getText());
            hiddenPasswordRegisterField.setVisible(true);
            notHiddenPasswordRegisterField.setVisible(false);

            hiddenConfirmPasswordField.setText(notHiddenConfirmPasswordField.getText());
            hiddenConfirmPasswordField.setVisible(true);
            notHiddenConfirmPasswordField.setVisible(false);
        }
    }

    private void goToMainScreen() {
        Pane root = App.getRoot();
        Parent page;
        URL mainScreenPath = null;
        if (App.currentUser instanceof Admin) {
            mainScreenPath = getClass().getResource("/fxml/AdminMainScreen.fxml");
        }
        else if (App.currentUser instanceof Member) {
            mainScreenPath = getClass().getResource("/fxml/MemberMainScreen.fxml");
        }

        try {
            assert mainScreenPath != null;
            page = FXMLLoader.load(mainScreenPath);
            root.getChildren().clear();
            root.getChildren().add(page);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}