package Menus.Tenants;

import Utilities.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ShowAddTenantForm {
    private final Stage stage;
    private final String userRole;

    public ShowAddTenantForm(Stage stage, String userRole) {
        this.stage = stage;
        this.userRole = userRole;
    }

    public void display() {
        Stage addTenantStage = new Stage();
        addTenantStage.setTitle("Add New Tenant");

        TextField nameField = new TextField();
        TextField emailField = new TextField();
        TextField phoneNumberField = new TextField();
        DatePicker moveInDatePicker = new DatePicker();
        DatePicker moveOutDatePicker = new DatePicker();
        TextField paymentHistoryField = new TextField();

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            try (Connection con = DBUtils.establishConnection();
                 PreparedStatement pstmt = con.prepareStatement(
                     "INSERT INTO Tenant (TenantName, EmailAddress, PhoneNumber, MoveInDate, MoveOutDate, PaymentHistory) VALUES (?, ?, ?, ?, ?, ?)")) {

                pstmt.setString(1, nameField.getText());
                pstmt.setString(2, emailField.getText());
                pstmt.setString(3, phoneNumberField.getText());
                pstmt.setDate(4, java.sql.Date.valueOf(moveInDatePicker.getValue()));
                pstmt.setDate(5, moveOutDatePicker.getValue() != null ? java.sql.Date.valueOf(moveOutDatePicker.getValue()) : null);
                pstmt.setString(6, paymentHistoryField.getText());
                pstmt.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Tenant Added");
                alert.setContentText("New tenant added successfully.");
                alert.showAndWait();

                new ShowTenants(stage, this.userRole).display();
                addTenantStage.close();
            } catch (SQLException ex) {
                ShowAlert.display("Database Error", "Failed to add new tenant.", Alert.AlertType.WARNING);
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

        Scene scene = new Scene(layout, 300, 500);
        addTenantStage.setScene(scene);
        addTenantStage.show();
    }
}