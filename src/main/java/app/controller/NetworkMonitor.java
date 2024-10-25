package app.controller;

import app.run.App;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NetworkMonitor{

    private final Parent noInternetScreen;
    private boolean isNoInternetDisplayed = false;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public NetworkMonitor() {
        try {
            noInternetScreen = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("/fxml/NoInternet.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startMonitoring() {
        scheduler.scheduleAtFixedRate(() -> {
            boolean internetAvailable = isInternetAvailable();
            Platform.runLater(() -> updateUI(internetAvailable));
        }, 0, 3, TimeUnit.SECONDS);
    }

    public void stopMonitoring() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(3, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }

    private static boolean isInternetAvailable() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("8.8.8.8", 53), 2000); // 2s timeout
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void updateUI(boolean internetAvailable) {
        Pane root = App.getRoot();
        if (!internetAvailable && !isNoInternetDisplayed) {
            root.getChildren().add(noInternetScreen);
            noInternetScreen.toFront();
            isNoInternetDisplayed = true;
        } else if (internetAvailable && isNoInternetDisplayed) {
            root.getChildren().remove(noInternetScreen);
            isNoInternetDisplayed = false;
        }
    }
}
