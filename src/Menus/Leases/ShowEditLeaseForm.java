package Menus.Leases;

import Utilities.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ShowEditLeaseForm {
    private final Stage stage;
    private final Lease lease;
    private final String userRole;

    public ShowEditLeaseForm(Stage stage, Lease lease, String userRole) {
        this.stage = stage;
        this.lease = lease;
        this.userRole = userRole;
    }

    public void display() {
        Stage editLeaseStage = new Stage();
        editLeaseStage.setTitle("Edit Lease");

        TextField propertyIdField = new TextField(String.valueOf(lease.getPropertyId()));
        TextField tenantIdField = new TextField(String.valueOf(lease.getTenantId()));
        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setValue(java.time.LocalDate.parse(lease.getStartDate()));
        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setValue(java.time.LocalDate.parse(lease.getEndDate()));
        TextField monthlyRentField = new TextField(lease.getMonthlyRent());
        TextField securityDepositField = new TextField(lease.getSecurityDeposit());
        DatePicker signatureDatePicker = new DatePicker();
        signatureDatePicker.setValue(java.time.LocalDate.parse(lease.getSignatureDate()));
        TextField statusField = new TextField(lease.getStatus());

        Button submitButton = new Button("Update");
        submitButton.setOnAction(e -> {
            try (Connection con = DBUtils.establishConnection();
                 PreparedStatement pstmt = con.prepareStatement(
                         "UPDATE LeaseAgreements SET PropertyID = ?, TenantID = ?, StartDate = ?, EndDate = ?, MonthlyRent = ?, SecurityDeposit = ?, SignatureDate = ?, Status = ? WHERE LeaseID = ?")) {

                pstmt.setInt(1, Integer.parseInt(propertyIdField.getText()));
                pstmt.setInt(2, Integer.parseInt(tenantIdField.getText()));
                pstmt.setDate(3, java.sql.Date.valueOf(startDatePicker.getValue()));
                pstmt.setDate(4, java.sql.Date.valueOf(endDatePicker.getValue()));
                pstmt.setString(5, monthlyRentField.getText());
                pstmt.setString(6, securityDepositField.getText());
                pstmt.setDate(7, java.sql.Date.valueOf(signatureDatePicker.getValue()));
                pstmt.setString(8, statusField.getText());
                pstmt.setInt(9, lease.getId());
                pstmt.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Lease Updated");
                alert.setContentText("Lease details updated successfully.");
                alert.showAndWait();

                new ShowLeases(stage, this.userRole).display();
                editLeaseStage.close();
            } catch (SQLException ex) {
                ShowAlert.display("Database Error", "Failed to update lease details.", Alert.AlertType.WARNING);
            }
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(
                new Label("Property ID:"), propertyIdField,
                new Label("Tenant ID:"), tenantIdField,
                new Label("Start Date:"), startDatePicker,
                new Label("End Date:"), endDatePicker,
                new Label("Monthly Rent:"), monthlyRentField,
                new Label("Security Deposit:"), securityDepositField,
                new Label("Signature Date:"), signatureDatePicker,
                new Label("Status:"), statusField,
                submitButton);

        Scene scene = new Scene(layout, 300, 500);
        editLeaseStage.setScene(scene);
        editLeaseStage.show();
    }
}