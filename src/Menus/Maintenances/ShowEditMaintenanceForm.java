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

public class ShowEditMaintenanceForm {
    private final Stage stage;
    private final Maintenance maintenance;
    private final String userRole;

    public ShowEditMaintenanceForm(Stage stage, Maintenance maintenance, String userRole) {
        this.stage = stage;
        this.maintenance = maintenance;
        this.userRole = userRole;
    }

    public void display() {
        Stage editMaintenanceStage = new Stage();
        editMaintenanceStage.setTitle("Edit Maintenance Request");

        TextField propertyIdField = new TextField(String.valueOf(maintenance.getPropertyId()));
        TextField tenantIdField = new TextField(String.valueOf(maintenance.getTenantId()));
        TextField descriptionField = new TextField(maintenance.getDescription());
        DatePicker reportDatePicker = new DatePicker();
        reportDatePicker.setValue(java.time.LocalDate.parse(maintenance.getReportDate()));
        TextField statusField = new TextField(maintenance.getStatus());
        TextField priorityField = new TextField(maintenance.getPriority());
        DatePicker resolutionDatePicker = new DatePicker();
        resolutionDatePicker.setValue(maintenance.getResolutionDate() != null ? java.time.LocalDate.parse(maintenance.getResolutionDate()) : null);

        Button submitButton = new Button("Update");
        submitButton.setOnAction(e -> {
            try (Connection con = DBUtils.establishConnection();
                 PreparedStatement pstmt = con.prepareStatement(
                         "UPDATE MaintenanceRequests SET PropertyID = ?, TenantID = ?, Description = ?, ReportDate = ?, Status = ?, Priority = ?, ResolutionDate = ? WHERE RequestID = ?")) {

                pstmt.setInt(1, Integer.parseInt(propertyIdField.getText()));
                pstmt.setInt(2, Integer.parseInt(tenantIdField.getText()));
                pstmt.setString(3, descriptionField.getText());
                pstmt.setDate(4, java.sql.Date.valueOf(reportDatePicker.getValue()));
                pstmt.setString(5, statusField.getText());
                pstmt.setString(6, priorityField.getText());
                pstmt.setDate(7, resolutionDatePicker.getValue() != null ? java.sql.Date.valueOf(resolutionDatePicker.getValue()) : null);
                pstmt.setInt(8, maintenance.getId());
                pstmt.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Maintenance Request Updated");
                alert.setContentText("Maintenance request details updated successfully.");
                alert.showAndWait();

                new ShowMaintenances(stage, this.userRole).display();
                editMaintenanceStage.close();
            } catch (SQLException ex) {
                ShowAlert.display("Database Error", "Failed to update maintenance request details.", Alert.AlertType.WARNING);
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
        editMaintenanceStage.setScene(scene);
        editMaintenanceStage.show();
    }
}