package Menus.Leases;

import Utilities.*;
import javafx.scene.control.Alert;
import java.sql.SQLException;

public class RemoveLease {

    public static void removeLease(int leaseId) {
        try {
            LeaseDAO.removeLeaseAgreement(leaseId);
            ShowAlert.display("Lease Removed", "The lease has been successfully removed.", Alert.AlertType.INFORMATION);
        } catch (SQLException ex) {
            ShowAlert.display("Database Error", "Failed to remove lease: " + ex.getMessage(), Alert.AlertType.WARNING);
        }
    }
}