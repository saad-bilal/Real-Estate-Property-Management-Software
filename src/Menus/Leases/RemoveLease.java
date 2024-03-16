package Menus.Leases;

import Utilities.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javafx.scene.control.Alert;

public class RemoveLease {

    public static void removeLease(int leaseId) {
        String sql = "DELETE FROM LeaseAgreements WHERE LeaseID = ?";

        try (Connection con = DBUtils.establishConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, leaseId);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                ShowAlert.display("Lease Removed", "The lease has been successfully removed.", Alert.AlertType.INFORMATION);
            } else {
                ShowAlert.display("Error", "No lease found with the specified ID.", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            ShowAlert.display("Database Error", "Failed to remove lease: " + e.getMessage(), Alert.AlertType.WARNING);
        }
    }
}