package app.run;

import app.base.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.awt.*;
import java.io.IOException;

public class App extends Application {
    public static User user;

    private static final String APP_NAME = "LibZone";
    private static final String LOGO_PATH = "/graphic/images/logo.png";
    private static final String START_FXML = "/fxml/Login.fxml";
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 633;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(START_FXML));
        Image logo = new Image(String.valueOf(App.class.getResource(LOGO_PATH)));

        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        stage.setResizable(false);
        stage.setTitle(APP_NAME);
        stage.getIcons().add(logo);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}