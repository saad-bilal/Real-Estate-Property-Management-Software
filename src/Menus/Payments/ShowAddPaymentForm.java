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

public class ShowAddPaymentForm {
    private final Stage stage;
    private final String userRole;

    public ShowAddPaymentForm(Stage stage, String userRole) {
        this.stage = stage;
        this.userRole = userRole;
    }

    public void display() {
        Stage addPaymentStage = new Stage();
        addPaymentStage.setTitle("Add New Payment");

        TextField leaseIdField = new TextField();
        TextField tenantIdField = new TextField();
        DatePicker paymentDatePicker = new DatePicker();
        TextField amountField = new TextField();
        TextField paymentTypeField = new TextField();
        TextField descriptionField = new TextField();
        TextField receiptNumberField = new TextField();

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            try (Connection con = DBUtils.establishConnection();
                 PreparedStatement pstmt = con.prepareStatement(
                    "INSERT INTO Payments (LeaseID, TenantID, PaymentDate, Amount, PaymentType, Description, ReceiptNumber) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

                pstmt.setInt(1, Integer.parseInt(leaseIdField.getText()));
                pstmt.setInt(2, Integer.parseInt(tenantIdField.getText()));
                pstmt.setDate(3, java.sql.Date.valueOf(paymentDatePicker.getValue()));
                pstmt.setString(4, amountField.getText());
                pstmt.setString(5, paymentTypeField.getText());
                pstmt.setString(6, descriptionField.getText());
                pstmt.setString(7, receiptNumberField.getText());
                pstmt.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Payment Added");
                alert.setContentText("New payment added successfully.");
                alert.showAndWait();

                new ShowPayments(stage, this.userRole).display();
                addPaymentStage.close();
            } catch (SQLException ex) {
                ShowAlert.display("Database Error", "Failed to add new payment.", Alert.AlertType.WARNING);
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
        addPaymentStage.setScene(scene);
        addPaymentStage.show();
    }
}