package Menus.Maintenances;

import Utilities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class RefreshTable {

    public static void refreshMaintenanceTable(TableView<Maintenance> maintenanceTable) {
        ObservableList<Maintenance> maintenances = FXCollections.observableArrayList();
        String query = "SELECT RequestID, PropertyID, TenantID, Description, ReportDate, Status, Priority, ResolutionDate FROM MaintenanceRequests";

        try (Connection con = DBUtils.establishConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Maintenance maintenance = new Maintenance(rs.getInt("RequestID"),
                                                         rs.getInt("PropertyID"),
                                                         rs.getInt("TenantID"),
                                                         rs.getString("Description"),
                                                         rs.getDate("ReportDate").toString(),
                                                         rs.getString("Status"),
                                                         rs.getString("Priority"),
                                                         rs.getDate("ResolutionDate") != null ? rs.getDate("ResolutionDate").toString() : null);
                maintenances.add(maintenance);
            }

            maintenanceTable.setItems(maintenances);

        } catch (Exception e) {
            ShowAlert.display("Database Error", "Failed to load maintenance requests: " + e.getMessage(), Alert.AlertType.WARNING);
        }
    }
}