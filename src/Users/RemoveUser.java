package Users;

import Utilities.*;
import javafx.scene.control.Alert;
import java.sql.SQLException;

public class RemoveUser {

    public static void removeUser(int userId) {
        try {
            UserDAO.removeUser(userId);
            ShowAlert.display("User Removed", "The user has been successfully removed.", Alert.AlertType.INFORMATION);
        } catch (SQLException ex) {
            ShowAlert.display("Database Error", "Failed to remove user: " + ex.getMessage(), Alert.AlertType.WARNING);
        }
    }
}