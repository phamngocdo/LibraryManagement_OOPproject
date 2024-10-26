package app.controller;

import app.run.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;

public class AdminMainScreen {

    @FXML
    private Pane functionPane;

    @FXML
    private Button homeButton, docEditButton, membersSearchButton, receiptsSearchButton, logoutButton;

    @FXML
    private void initialize() {
        goToFunction("Home");
    }

    @FXML
    private void onHome() {
        goToFunction("Home");
    }

    @FXML
    private void onDocEdit() {
        goToFunction("DocEdit");
    }

    @FXML
    private void onMembersSearch() {
        goToFunction("MembersSearching");
    }

    @FXML
    private void onReceiptsSearch() {
        goToFunction("ReceiptsSearching");
    }

    @FXML
    private void onLogout() {
        Pane root = App.getRoot();
        try {
            URL loginFxmlUrl = getClass().getResource("/fxml/Login.fxml");
            if (loginFxmlUrl != null) {
                Parent loginRoot = FXMLLoader.load(loginFxmlUrl);
                root.getChildren().clear();
                root.getChildren().add(loginRoot);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void goToFunction(String name) {
        try {
            URL fxmlUrl = getClass().getResource("/fxml/" + name + ".fxml");
            if (fxmlUrl != null) {
                Parent page = FXMLLoader.load(fxmlUrl);
                functionPane.getChildren().clear();
                functionPane.getChildren().add(page);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}