package Menus.Maintenances;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Utilities.DBUtils;
import java.sql.Statement;

public class MaintenanceDAO {

    // Method to retrieve all maintenance requests
    public static List<Maintenance> getAllMaintenanceRequests() throws SQLException {
        List<Maintenance> maintenanceRequests = new ArrayList<>();
        String query = "SELECT RequestID, PropertyID, TenantID, Description, ReportDate, Status, Priority, ResolutionDate FROM MaintenanceRequests";

        try (Connection con = DBUtils.establishConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Maintenance maintenance = new Maintenance(rs.getInt("RequestID"), rs.getInt("PropertyID"),
                        rs.getInt("TenantID"),
                        rs.getString("Description"), rs.getDate("ReportDate").toString(), rs.getString("Status"),
                        rs.getString("Priority"),
                        rs.getDate("ResolutionDate") != null ? rs.getDate("ResolutionDate").toString() : null);
                maintenanceRequests.add(maintenance);
            }
        }
        return maintenanceRequests;
    }

    // Method to add a new maintenance request
    public static void addMaintenanceRequest(int propertyId, int tenantId, String description, String reportDate,
            String status, String priority, String resolutionDate) throws SQLException {
        String sql = "INSERT INTO MaintenanceRequests (PropertyID, TenantID, Description, ReportDate, Status, Priority, ResolutionDate) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBUtils.establishConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, propertyId);
            pstmt.setInt(2, tenantId);
            pstmt.setString(3, description);
            pstmt.setDate(4, java.sql.Date.valueOf(reportDate));
            pstmt.setString(5, status);
            pstmt.setString(6, priority);
            pstmt.setDate(7, resolutionDate != null ? java.sql.Date.valueOf(resolutionDate) : null);
            pstmt.executeUpdate();
        }
    }

    // Method to update an existing maintenance request
    public static void updateMaintenanceRequest(int requestId, int propertyId, int tenantId, String description,
            String reportDate, String status, String priority, String resolutionDate) throws SQLException {
        String sql = "UPDATE MaintenanceRequests SET PropertyID = ?, TenantID = ?, Description = ?, ReportDate = ?, Status = ?, Priority = ?, ResolutionDate = ? WHERE RequestID = ?";
        try (Connection con = DBUtils.establishConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, propertyId);
            pstmt.setInt(2, tenantId);
            pstmt.setString(3, description);
            pstmt.setDate(4, java.sql.Date.valueOf(reportDate));
            pstmt.setString(5, status);
            pstmt.setString(6, priority);
            pstmt.setDate(7, resolutionDate != null ? java.sql.Date.valueOf(resolutionDate) : null);
            pstmt.setInt(8, requestId);
            pstmt.executeUpdate();
        }
    }

    // Method to remove a maintenance request
    public static void removeMaintenanceRequest(int requestId) throws SQLException {
        String sql = "DELETE FROM MaintenanceRequests WHERE RequestID = ?";
        try (Connection con = DBUtils.establishConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, requestId);
            pstmt.executeUpdate();
        }
    }

}