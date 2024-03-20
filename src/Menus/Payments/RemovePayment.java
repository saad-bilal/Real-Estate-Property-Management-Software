package Menus.Payments;

import Utilities.*;
import javafx.scene.control.Alert;
import java.sql.SQLException;

public class RemovePayment {

    public static void removePayment(int paymentId) {
        try {
            PaymentDAO.removePayment(paymentId);
            ShowAlert.display("Payment Removed", "The payment has been successfully removed.", Alert.AlertType.INFORMATION);
        } catch (SQLException ex) {
            ShowAlert.display("Database Error", "Failed to remove payment: " + ex.getMessage(), Alert.AlertType.WARNING);
        }
    }
}