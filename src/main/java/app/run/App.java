package app.run;

import app.base.Admin;
import app.base.User;
import app.controller.NetworkMonitor;
import app.database.DatabaseManagement;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.io.IOException;

public class App extends Application {
    public static User currentUser;

    private static Scene scene;

    private static final String APP_NAME = "LibZone";
    private static final String LOGO_PATH = "/graphic/images/logo.png";
    private static final String START_FXML = "/fxml/DocumentEdit.fxml";
    private static final String CSS_PATH = "/graphic/css/style.css";
    private static final int WIDTH = 1126;
    private static final int HEIGHT = 650;

    @Override
    public void start(Stage stage) throws IOException {
        DatabaseManagement.setConnection();

        // Khởi tạo currentUser là Admin
        currentUser = new Admin("admin_id", "admin", "admin", "Admin", "User", "01/01/1990", "admin@libzone.com", "0123456789");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(START_FXML));
        Image logo = new Image(String.valueOf(getClass().getResource(LOGO_PATH)));

        scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        scene.getStylesheets().add(String.valueOf(getClass().getResource(CSS_PATH)));
        stage.setResizable(false);
        stage.setTitle(APP_NAME);
        stage.getIcons().add(logo);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> System.exit(0));
        stage.show();

        NetworkMonitor networkMonitor = new NetworkMonitor();
        networkMonitor.startMonitoring();
    }

    public static Pane getRoot() {
        return (Pane) scene.getRoot();
    }

    public static void close() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch();
    }
}
