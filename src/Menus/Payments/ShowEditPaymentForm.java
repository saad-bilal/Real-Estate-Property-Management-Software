package Menus.Payments;

import Utilities.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ShowEditPaymentForm {
    private final Stage stage;
    private final Payment payment;
    private final String userRole;

    public ShowEditPaymentForm(Stage stage, Payment payment, String userRole) {
        this.stage = stage;
        this.payment = payment;
        this.userRole = userRole;
    }

    public void display() {
        Stage editPaymentStage = new Stage();
        editPaymentStage.setTitle("Edit Payment");

        TextField leaseIdField = new TextField(String.valueOf(payment.getLeaseId()));
        TextField tenantIdField = new TextField(String.valueOf(payment.getTenantId()));
        DatePicker paymentDatePicker = new DatePicker();
        paymentDatePicker.setValue(java.time.LocalDate.parse(payment.getPaymentDate()));
        TextField amountField = new TextField(payment.getAmount());
        TextField paymentTypeField = new TextField(payment.getPaymentType());
        TextField descriptionField = new TextField(payment.getDescription());
        TextField receiptNumberField = new TextField(payment.getReceiptNumber());

        Button submitButton = new Button("Update");
        submitButton.setOnAction(e -> {
            try (Connection con = DBUtils.establishConnection();
                 PreparedStatement pstmt = con.prepareStatement(
                         "UPDATE Payments SET LeaseID = ?, TenantID = ?, PaymentDate = ?, Amount = ?, PaymentType = ?, Description = ?, ReceiptNumber = ? WHERE PaymentID = ?")) {

                pstmt.setInt(1, Integer.parseInt(leaseIdField.getText()));
                pstmt.setInt(2, Integer.parseInt(tenantIdField.getText()));
                pstmt.setDate(3, java.sql.Date.valueOf(paymentDatePicker.getValue()));
                pstmt.setString(4, amountField.getText());
                pstmt.setString(5, paymentTypeField.getText());
                pstmt.setString(6, descriptionField.getText());
                pstmt.setString(7, receiptNumberField.getText());
                pstmt.setInt(8, payment.getId());
                pstmt.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Payment Updated");
                alert.setContentText("Payment details updated successfully.");
                alert.showAndWait();

                new ShowPayments(stage, this.userRole).display();
                editPaymentStage.close();
            } catch (SQLException ex) {
                ShowAlert.display("Database Error", "Failed to update payment details.", Alert.AlertType.WARNING);
            }
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(
                new Label("Lease ID:"), leaseIdField,
                new Label("Tenant ID:"), tenantIdField,
                new Label("Payment Date:"), paymentDatePicker,
                new Label("Amount:"), amountField,
                new Label("Payment Type:"), paymentTypeField,
                new Label("Description:"), descriptionField,
                new Label("Receipt Number:"), receiptNumberField,
                submitButton);

        Scene scene = new Scene(layout, 300, 500);
        editPaymentStage.setScene(scene);
        editPaymentStage.show();
    }
}