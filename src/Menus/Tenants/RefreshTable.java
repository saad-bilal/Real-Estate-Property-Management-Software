package Menus.Tenants;

import Utilities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class RefreshTable {

    public static void refreshTenantsTable(TableView<Tenant> tenantsTable) {
        ObservableList<Tenant> tenants = FXCollections.observableArrayList();
        String query = "SELECT TenantID, TenantName, EmailAddress, PhoneNumber, MoveInDate, MoveOutDate FROM Tenant";

        try (Connection con = DBUtils.establishConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Tenant tenant = new Tenant(rs.getInt("TenantID"),
                                 rs.getString("TenantName"),
                                 rs.getString("EmailAddress"),
                                 rs.getString("PhoneNumber"),
                                 rs.getDate("MoveInDate").toString(),
                                 rs.getDate("MoveOutDate") != null ? rs.getDate("MoveOutDate").toString() : "",
                                 rs.getString("PaymentHistory"));
                tenants.add(tenant);
            }

            tenantsTable.setItems(tenants);

        } catch (Exception e) {
            ShowAlert.display("Database Error", "Failed to load tenants: " + e.getMessage(), Alert.AlertType.WARNING);
        }
    }
}