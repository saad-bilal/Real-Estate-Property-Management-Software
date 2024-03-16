package Menus.Properties;

import Utilities.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
            try (Connection con = DBUtils.establishConnection();
                    PreparedStatement pstmt = con.prepareStatement(
                            "UPDATE Property SET Type = ?, Size = ?, Location = ?, Price = ?, FurnishingStatus = ?, MaintenanceHistory = ? WHERE PropertyID = ?")) {

                pstmt.setString(1, typeField.getText());
                pstmt.setString(2, sizeField.getText());
                pstmt.setString(3, locationField.getText());
                pstmt.setString(4, priceField.getText());
                pstmt.setString(5, furnishingStatusField.getText());
                pstmt.setString(6, maintenanceHistoryField.getText());
                pstmt.setInt(7, property.getId());
                pstmt.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Property Updated");
                alert.setContentText("Property details updated successfully.");
                alert.showAndWait();

                new ShowProperties(stage, this.userRole).display();
                editPropertyStage.close();
            } catch (SQLException ex) {
                ShowAlert.display("Database Error", "Failed to update property details.", Alert.AlertType.WARNING);
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