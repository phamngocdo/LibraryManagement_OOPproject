module app {
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires com.jfoenix;
    requires javafx.controls;

    opens app.run to javafx.fxml;
    opens app.controller to javafx.fxml;
    opens app.base to javafx.fxml;
    opens app.service to javafx.fxml;

    exports app.run;
    exports app.controller;
    exports app.base;
    exports app.service;
}
