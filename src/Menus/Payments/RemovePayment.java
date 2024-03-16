package Menus.Payments;

import Utilities.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javafx.scene.control.Alert;

public class RemovePayment {

    public static void removePayment(int paymentId) {
        String sql = "DELETE FROM Payments WHERE PaymentID = ?";

        try (Connection con = DBUtils.establishConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, paymentId);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                ShowAlert.display("Payment Removed", "The payment has been successfully removed.", Alert.AlertType.INFORMATION);
            } else {
                ShowAlert.display("Error", "No payment found with the specified ID.", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            ShowAlert.display("Database Error", "Failed to remove payment: " + e.getMessage(), Alert.AlertType.WARNING);
        }
    }
}