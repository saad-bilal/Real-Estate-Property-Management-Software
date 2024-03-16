package Users;

import Utilities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class RefreshTable {

    public static void refreshUsersTable(TableView<User> usersTable) {
        ObservableList<User> users = FXCollections.observableArrayList();
        String query = "SELECT UserID, FirstName, LastName, Username, Email, PhoneNumber, Role FROM Users";

        try (Connection con = DBUtils.establishConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                User user = new User(rs.getInt("UserID"),
                                     rs.getString("FirstName"),
                                     rs.getString("LastName"),
                                     rs.getString("Username"),
                                     rs.getString("Email"),
                                     rs.getString("PhoneNumber"),
                                     rs.getString("Role"));
                users.add(user);
            }

            usersTable.setItems(users);

        } catch (Exception e) {
            ShowAlert.display("Database Error", "Failed to load users: " + e.getMessage(), Alert.AlertType.WARNING);
        }
    }
}