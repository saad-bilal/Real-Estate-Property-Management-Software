package Menus.Maintenances;

import Utilities.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ShowAddMaintenanceForm {
    private final Stage stage;
    private final String userRole;

    public ShowAddMaintenanceForm(Stage stage, String userRole) {
        this.stage = stage;
        this.userRole = userRole;
    }

    public void display() {
        Stage addMaintenanceRequestStage = new Stage();
        addMaintenanceRequestStage.setTitle("Add New Maintenance Request");

        TextField propertyIdField = new TextField();
        TextField tenantIdField = new TextField();
        TextField descriptionField = new TextField();
        DatePicker reportDatePicker = new DatePicker();
        TextField statusField = new TextField();
        TextField priorityField = new TextField();
        DatePicker resolutionDatePicker = new DatePicker();

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            try (Connection con = DBUtils.establishConnection();
                 PreparedStatement pstmt = con.prepareStatement(
                    "INSERT INTO MaintenanceRequests (PropertyID, TenantID, Description, ReportDate, Status, Priority, ResolutionDate) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

                pstmt.setInt(1, Integer.parseInt(propertyIdField.getText()));
                pstmt.setInt(2, Integer.parseInt(tenantIdField.getText()));
                pstmt.setString(3, descriptionField.getText());
                pstmt.setDate(4, java.sql.Date.valueOf(reportDatePicker.getValue()));
                pstmt.setString(5, statusField.getText());
                pstmt.setString(6, priorityField.getText());
                pstmt.setDate(7, resolutionDatePicker.getValue() != null ? java.sql.Date.valueOf(resolutionDatePicker.getValue()) : null);
                pstmt.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Maintenance Request Added");
                alert.setContentText("New maintenance request added successfully.");
                alert.showAndWait();

                new ShowMaintenances(stage, this.userRole).display();
                addMaintenanceRequestStage.close();
            } catch (SQLException ex) {
                ShowAlert.display("Database Error", "Failed to add new maintenance request.", Alert.AlertType.WARNING);
            }
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(
                new Label("Property ID:"), propertyIdField,
                new Label("Tenant ID:"), tenantIdField,
                new Label("Description:"), descriptionField,
                new Label("Report Date:"), reportDatePicker,
                new Label("Status:"), statusField,
                new Label("Priority:"), priorityField,
                new Label("Resolution Date:"), resolutionDatePicker,
                submitButton);

        Scene scene = new Scene(layout, 300, 500);
        addMaintenanceRequestStage.setScene(scene);
        addMaintenanceRequestStage.show();
    }
}