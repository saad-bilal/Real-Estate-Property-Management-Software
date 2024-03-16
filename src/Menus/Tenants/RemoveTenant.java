package Menus.Tenants;

import Utilities.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javafx.scene.control.Alert;

public class RemoveTenant {

    public static void removeTenant(int tenantId) {
        String sql = "DELETE FROM Tenant WHERE TenantID = ?";

        try (Connection con = DBUtils.establishConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, tenantId);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                ShowAlert.display("Tenant Removed", "The tenant has been successfully removed.", Alert.AlertType.INFORMATION);
            } else {
                ShowAlert.display("Error", "No tenant found with the specified ID.", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            ShowAlert.display("Database Error", "Failed to remove tenant: " + e.getMessage(), Alert.AlertType.WARNING);
        }
    }
}