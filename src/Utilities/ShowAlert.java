package Utilities;

import javafx.scene.control.Alert;

public class ShowAlert {
    public static void display(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}