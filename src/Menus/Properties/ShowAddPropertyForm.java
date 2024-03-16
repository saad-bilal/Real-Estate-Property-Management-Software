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
            try (Connection con = DBUtils.establishConnection();
                 PreparedStatement pstmt = con.prepareStatement(
                    "INSERT INTO Property (Type, Size, Location, Price, FurnishingStatus, MaintenanceHistory) VALUES (?, ?, ?, ?, ?, ?)")) {

                pstmt.setString(1, typeField.getText());
                pstmt.setString(2, sizeField.getText());
                pstmt.setString(3, locationField.getText());
                pstmt.setString(4, priceField.getText());
                pstmt.setString(5, furnishingStatusField.getText());
                pstmt.setString(6, maintenanceHistoryField.getText());
                pstmt.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Property Added");
                alert.setContentText("New property added successfully.");
                alert.showAndWait();

                new ShowProperties(stage, this.userRole).display();
                addPropertyStage.close();
            } catch (SQLException ex) {
                ShowAlert.display("Database Error", "Failed to add new property.", Alert.AlertType.WARNING);
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