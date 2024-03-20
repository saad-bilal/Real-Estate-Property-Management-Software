package Menus.Tenants;

import Utilities.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import java.sql.SQLException;

public class ShowEditTenantForm {
    private final Stage stage;
    private final Tenant tenant;
    private final String userRole;

    public ShowEditTenantForm(Stage stage, Tenant tenant, String userRole) {
        this.stage = stage;
        this.tenant = tenant;
        this.userRole = userRole;
    }

    public void display() {
        Stage editTenantStage = new Stage();
        editTenantStage.setTitle("Edit Tenant");

        TextField nameField = new TextField(tenant.getName());
        TextField emailField = new TextField(tenant.getEmail());
        TextField phoneNumberField = new TextField(tenant.getPhoneNumber());
        DatePicker moveInDatePicker = new DatePicker();
        moveInDatePicker.setValue(java.time.LocalDate.parse(tenant.getMoveInDate()));
        DatePicker moveOutDatePicker = new DatePicker();
        if (!tenant.getMoveOutDate().isEmpty()) {
            moveOutDatePicker.setValue(java.time.LocalDate.parse(tenant.getMoveOutDate()));
        }
        TextField paymentHistoryField = new TextField(tenant.getPaymentHistory());

        Button submitButton = new Button("Update");
        submitButton.setOnAction(e -> {
            try {
                TenantDAO.updateTenant(
                        tenant.getId(),
                        nameField.getText(),
                        emailField.getText(),
                        phoneNumberField.getText(),
                        moveInDatePicker.getValue().toString(),
                        moveOutDatePicker.getValue() != null ? moveOutDatePicker.getValue().toString() : null,
                        paymentHistoryField.getText());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Tenant Updated");
                alert.setContentText("Tenant details updated successfully.");
                alert.showAndWait();

                new ShowTenants(stage, this.userRole).display();
                editTenantStage.close();
            } catch (SQLException ex) {
                ShowAlert.display("Database Error", "Failed to update tenant details.", Alert.AlertType.WARNING);
            }
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(
                new Label("Tenant Name:"), nameField,
                new Label("Email Address:"), emailField,
                new Label("Phone Number:"), phoneNumberField,
                new Label("Move-in Date:"), moveInDatePicker,
                new Label("Move-out Date:"), moveOutDatePicker,
                new Label("Payment History:"), paymentHistoryField,
                submitButton);

        Scene scene = new Scene(layout, 300, 430);
        editTenantStage.setScene(scene);
        editTenantStage.show();
    }
}