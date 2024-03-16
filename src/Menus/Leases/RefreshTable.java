package Menus.Leases;

import Utilities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class RefreshTable {

    public static void refreshLeasesTable(TableView<Lease> leasesTable) {
        ObservableList<Lease> leases = FXCollections.observableArrayList();
        String query = "SELECT LeaseID, PropertyID, TenantID, StartDate, EndDate, MonthlyRent, SecurityDeposit, SignatureDate, Status FROM LeaseAgreements";

        try (Connection con = DBUtils.establishConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Lease lease = new Lease(rs.getInt("LeaseID"),
                                         rs.getInt("PropertyID"),
                                         rs.getInt("TenantID"),
                                         rs.getDate("StartDate").toString(),
                                         rs.getDate("EndDate").toString(),
                                         rs.getString("MonthlyRent"),
                                         rs.getString("SecurityDeposit"),
                                         rs.getDate("SignatureDate").toString(),
                                         rs.getString("Status"));
                leases.add(lease);
            }

            leasesTable.setItems(leases);

        } catch (Exception e) {
            ShowAlert.display("Database Error", "Failed to load leases: " + e.getMessage(), Alert.AlertType.WARNING);
        }
    }
}