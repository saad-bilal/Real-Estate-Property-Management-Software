package Menus.Maintenances;

import Utilities.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javafx.scene.control.Alert;

public class RemoveMaintenance {

    public static void removeMaintenance(int requestId) {
        String sql = "DELETE FROM Maintenance WHERE RequestID = ?";

        try (Connection con = DBUtils.establishConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, requestId);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                ShowAlert.display("Maintenance Request Removed", "The maintenance request has been successfully removed.", Alert.AlertType.INFORMATION);
            } else {
                ShowAlert.display("Error", "No maintenance request found with the specified ID.", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            ShowAlert.display("Database Error", "Failed to remove maintenance request: " + e.getMessage(), Alert.AlertType.WARNING);
        }
    }
}