package Menus.Maintenances;

import Utilities.*;
import javafx.scene.control.Alert;
import java.sql.SQLException;

public class RemoveMaintenance {

    public static void removeMaintenance(int requestId) {
        try {
            MaintenanceDAO.removeMaintenanceRequest(requestId);
            ShowAlert.display("Maintenance Request Removed", "The maintenance request has been successfully removed.", Alert.AlertType.INFORMATION);
        } catch (SQLException ex) {
            ShowAlert.display("Database Error", "Failed to remove maintenance request: " + ex.getMessage(), Alert.AlertType.WARNING);
        }
    }
}