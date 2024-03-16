package Menus.Properties;

import Utilities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class RefreshTable {

    public static void refreshPropertiesTable(TableView<Property> propertiesTable) {
        ObservableList<Property> properties = FXCollections.observableArrayList();
        String query = "SELECT PropertyID, Type, Size, Location, Price, FurnishingStatus, MaintenanceHistory FROM Property";

        try (Connection con = DBUtils.establishConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Property property = new Property(rs.getInt("PropertyID"),
                                                 rs.getString("Type"),
                                                 rs.getString("Size"),
                                                 rs.getString("Location"),
                                                 rs.getString("Price"),
                                                 rs.getString("FurnishingStatus"),
                                                 rs.getString("MaintenanceHistory"));
                properties.add(property);
            }

            propertiesTable.setItems(properties);

        } catch (Exception e) {
            ShowAlert.display("Database Error", "Failed to load properties: " + e.getMessage(), Alert.AlertType.WARNING);
        }
    }
}