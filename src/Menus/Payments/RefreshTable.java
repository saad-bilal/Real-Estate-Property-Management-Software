package Menus.Payments;

import Utilities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class RefreshTable {

    public static void refreshPaymentsTable(TableView<Payment> paymentsTable) {
        ObservableList<Payment> payments = FXCollections.observableArrayList();
        String query = "SELECT PaymentID, LeaseID, TenantID, PaymentDate, Amount, PaymentType, Description, ReceiptNumber FROM Payments";

        try (Connection con = DBUtils.establishConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Payment payment = new Payment(rs.getInt("PaymentID"),
                                             rs.getInt("LeaseID"),
                                             rs.getInt("TenantID"),
                                             rs.getDate("PaymentDate").toString(),
                                             rs.getString("Amount"),
                                             rs.getString("PaymentType"),
                                             rs.getString("Description"),
                                             rs.getString("ReceiptNumber"));
                payments.add(payment);
            }

            paymentsTable.setItems(payments);

        } catch (Exception e) {
            ShowAlert.display("Database Error", "Failed to load payments: " + e.getMessage(), Alert.AlertType.WARNING);
        }
    }
}