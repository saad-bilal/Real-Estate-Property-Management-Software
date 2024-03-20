package Menus.Properties;

import Utilities.ShowAlert;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import java.sql.SQLException;

public class ShowEditPropertyForm {
    private final Stage stage;
    private final Property property;
    private final String userRole;

    public ShowEditPropertyForm(Stage stage, Property property, String userRole) {
        this.stage = stage;
        this.property = property;
        this.userRole = userRole;
    }

    public void display() {
        Stage editPropertyStage = new Stage();
        editPropertyStage.setTitle("Edit Property");

        TextField typeField = new TextField(property.getType());
        TextField sizeField = new TextField(property.getSize());
        TextField locationField = new TextField(property.getLocation());
        TextField priceField = new TextField(property.getPrice());
        TextField furnishingStatusField = new TextField(property.getFurnishingStatus());
        TextField maintenanceHistoryField = new TextField(property.getMaintenanceHistory());

        Button submitButton = new Button("Update");
        submitButton.setOnAction(e -> {
            try {
                PropertyDAO.updateProperty(
                        property.getId(),
                        typeField.getText(),
                        sizeField.getText(),
                        locationField.getText(),
                        priceField.getText(),
                        furnishingStatusField.getText(),
                        maintenanceHistoryField.getText());
                ShowAlert.display("Property Updated", "Property details updated successfully.",
                        Alert.AlertType.INFORMATION);
                new ShowProperties(stage, this.userRole).display();
                editPropertyStage.close();
            } catch (SQLException ex) {
                ShowAlert.display("Database Error", "Failed to update property details: " + ex.getMessage(),
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

        Scene scene = new Scene(layout, 300, 430);
        editPropertyStage.setScene(scene);
        editPropertyStage.show();
    }
}