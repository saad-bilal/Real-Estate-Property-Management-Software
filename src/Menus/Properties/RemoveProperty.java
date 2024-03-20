package Menus.Properties;

import Utilities.ShowAlert;
import javafx.scene.control.Alert;
import java.sql.SQLException;

public class RemoveProperty {

    public static void removeProperty(int propertyId) {
        try {
            PropertyDAO.removeProperty(propertyId);
            ShowAlert.display("Property Removed", "The property has been successfully removed.",
                    Alert.AlertType.INFORMATION);
        } catch (SQLException ex) {
            ShowAlert.display("Database Error", "Failed to remove property: " + ex.getMessage(),
                    Alert.AlertType.WARNING);
        }
    }
}