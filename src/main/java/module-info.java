module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens app.run to javafx.fxml;
    opens app.controller to javafx.fxml;

    exports app.run;
    exports app.controller;
}
