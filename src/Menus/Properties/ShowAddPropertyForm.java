package Menus.Properties;

import Utilities.ShowAlert;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import java.sql.SQLException;

public class ShowAddPropertyForm {
    private final Stage stage;
    private final String userRole;

    public ShowAddPropertyForm(Stage stage, String userRole) {
        this.stage = stage;
        this.userRole = userRole;
    }

    public void display() {
        Stage addPropertyStage = new Stage();
        addPropertyStage.setTitle("Add New Property");

        TextField typeField = new TextField();
        TextField sizeField = new TextField();
        TextField locationField = new TextField();
        TextField priceField = new TextField();
        TextField furnishingStatusField = new TextField();
        TextField maintenanceHistoryField = new TextField();

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            try {
                PropertyDAO.addProperty(
                        typeField.getText(),
                        sizeField.getText(),
                        locationField.getText(),
                        priceField.getText(),
                        furnishingStatusField.getText(),
                        maintenanceHistoryField.getText());
                ShowAlert.display("Property Added", "New property added successfully.", Alert.AlertType.INFORMATION);
                new ShowProperties(stage, userRole).display(); // Refresh the table
                addPropertyStage.close();
            } catch (SQLException ex) {
                ShowAlert.display("Database Error", "Failed to add new property: " + ex.getMessage(),
                        Alert.AlertType.WARNING);
            }
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(
                new Label("Type:"), typeField,
                new Label("Size:"), sizeField,
                new Label("Location:"), locationField,
                new Label("Price:"), priceField,
                new Label("Furnishing Status:"), furnishingStatusField,
                new Label("Maintenance History:"), maintenanceHistoryField,
                submitButton);

        Scene scene = new Scene(layout, 300, 500);
        addPropertyStage.setScene(scene);
        addPropertyStage.show();
    }
}