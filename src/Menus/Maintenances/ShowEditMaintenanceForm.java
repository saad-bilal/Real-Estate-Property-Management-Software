package Menus.Maintenances;

import Utilities.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
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
            try {
                MaintenanceDAO.updateMaintenanceRequest(
                        maintenance.getId(),
                        Integer.parseInt(propertyIdField.getText()),
                        Integer.parseInt(tenantIdField.getText()),
                        descriptionField.getText(),
                        reportDatePicker.getValue().toString(),
                        statusField.getText(),
                        priorityField.getText(),
                        resolutionDatePicker.getValue() != null ? resolutionDatePicker.getValue().toString() : null);

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