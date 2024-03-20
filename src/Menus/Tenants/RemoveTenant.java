package Menus.Tenants;

import Utilities.*;
import javafx.scene.control.Alert;
import java.sql.SQLException;

public class RemoveTenant {

    public static void removeTenant(int tenantId) {
        try {
            TenantDAO.removeTenant(tenantId);
            ShowAlert.display("Tenant Removed", "The tenant has been successfully removed.",
                    Alert.AlertType.INFORMATION);
        } catch (SQLException ex) {
            ShowAlert.display("Database Error", "Failed to remove tenant: " + ex.getMessage(), Alert.AlertType.WARNING);
        }
    }
}