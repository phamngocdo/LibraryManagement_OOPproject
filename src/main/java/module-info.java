module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires java.desktop;
    requires org.json;
    requires com.google.zxing;
    requires com.google.zxing.javase;

    opens app.run to javafx.fxml;
    opens app.controller to javafx.fxml;
    opens app.base to javafx.fxml;
    opens app.service to javafx.fxml;

    exports app.run;
    exports app.controller;
    exports app.base;
    exports app.service;
}
