package Menus.Properties;

import Utilities.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javafx.scene.control.Alert;

public class RemoveProperty {

    public static void removeProperty(int propertyId) {
        String sql = "DELETE FROM Property WHERE PropertyID = ?";

        try (Connection con = DBUtils.establishConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, propertyId);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                ShowAlert.display("Property Removed", "The property has been successfully removed.", Alert.AlertType.INFORMATION);
            } else {
                ShowAlert.display("Error", "No property found with the specified ID.", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            ShowAlert.display("Database Error", "Failed to remove property: " + e.getMessage(), Alert.AlertType.WARNING);
        }
    }
}