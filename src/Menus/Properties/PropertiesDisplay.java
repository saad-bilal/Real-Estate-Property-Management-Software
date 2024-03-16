package Menus.Properties;

import javafx.stage.Stage;

public class PropertiesDisplay {
    private final Stage stage;
    private final String userRole;

    public PropertiesDisplay(Stage stage, String userRole) {
        this.stage = stage;
        this.userRole = userRole;
    }

    public void showProperties() {
        new ShowProperties(stage, userRole).display();
    }

    public static void main(String[] args) {
        // This main method would be used to launch the application
    }
}