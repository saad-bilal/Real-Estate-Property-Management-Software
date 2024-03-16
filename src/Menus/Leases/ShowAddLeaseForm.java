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

public class ShowAddLeaseForm {
    private final Stage stage;
    private final String userRole;

    public ShowAddLeaseForm(Stage stage, String userRole) {
        this.stage = stage;
        this.userRole = userRole;
    }

    public void display() {
        Stage addLeaseStage = new Stage();
        addLeaseStage.setTitle("Add New Lease");

        TextField propertyIdField = new TextField();
        TextField tenantIdField = new TextField();
        DatePicker startDatePicker = new DatePicker();
        DatePicker endDatePicker = new DatePicker();
        TextField monthlyRentField = new TextField();
        TextField securityDepositField = new TextField();
        DatePicker signatureDatePicker = new DatePicker();
        TextField statusField = new TextField();

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            try (Connection con = DBUtils.establishConnection();
                 PreparedStatement pstmt = con.prepareStatement(
                    "INSERT INTO LeaseAgreements (PropertyID, TenantID, StartDate, EndDate, MonthlyRent, SecurityDeposit, SignatureDate, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

                pstmt.setInt(1, Integer.parseInt(propertyIdField.getText()));
                pstmt.setInt(2, Integer.parseInt(tenantIdField.getText()));
                pstmt.setDate(3, java.sql.Date.valueOf(startDatePicker.getValue()));
                pstmt.setDate(4, java.sql.Date.valueOf(endDatePicker.getValue()));
                pstmt.setString(5, monthlyRentField.getText());
                pstmt.setString(6, securityDepositField.getText());
                pstmt.setDate(7, java.sql.Date.valueOf(signatureDatePicker.getValue()));
                pstmt.setString(8, statusField.getText());
                pstmt.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Lease Added");
                alert.setContentText("New lease added successfully.");
                alert.showAndWait();

                new ShowLeases(stage, this.userRole).display();
                addLeaseStage.close();
            } catch (SQLException ex) {
                ShowAlert.display("Database Error", "Failed to add new lease.", Alert.AlertType.WARNING);
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
        addLeaseStage.setScene(scene);
        addLeaseStage.show();
    }
}