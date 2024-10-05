package app.run;

import app.database.DatabaseManagement;

public class RunApp {
    public static void main(String[] args) {
        DatabaseManagement.setConnection();
        App.main(args);
        DatabaseManagement.closeConnection();
    }
}
