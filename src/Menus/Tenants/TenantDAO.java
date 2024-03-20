package Menus.Tenants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Utilities.DBUtils;

public class TenantDAO {

    // Method to retrieve all tenants
    public static List<Tenant> getAllTenants() throws SQLException {
        List<Tenant> tenants = new ArrayList<>();
        String query = "SELECT TenantID, TenantName, EmailAddress, PhoneNumber, MoveInDate, MoveOutDate, PaymentHistory FROM Tenant";

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
        }
        return tenants;
    }

    // Method to add a new tenant
    public static void addTenant(String name, String email, String phoneNumber, String moveInDate, String moveOutDate,
            String paymentHistory) throws SQLException {
        String sql = "INSERT INTO Tenant (TenantName, EmailAddress, PhoneNumber, MoveInDate, MoveOutDate, PaymentHistory) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBUtils.establishConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, phoneNumber);
            pstmt.setDate(4, java.sql.Date.valueOf(moveInDate));
            pstmt.setDate(5, moveOutDate != null ? java.sql.Date.valueOf(moveOutDate) : null);
            pstmt.setString(6, paymentHistory);
            pstmt.executeUpdate();
        }
    }

    // Method to update an existing tenant
    public static void updateTenant(int tenantId, String name, String email, String phoneNumber, String moveInDate,
            String moveOutDate, String paymentHistory) throws SQLException {
        String sql = "UPDATE Tenant SET TenantName = ?, EmailAddress = ?, PhoneNumber = ?, MoveInDate = ?, MoveOutDate = ?, PaymentHistory = ? WHERE TenantID = ?";
        try (Connection con = DBUtils.establishConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, phoneNumber);
            pstmt.setDate(4, java.sql.Date.valueOf(moveInDate));
            pstmt.setDate(5, moveOutDate != null ? java.sql.Date.valueOf(moveOutDate) : null);
            pstmt.setString(6, paymentHistory);
            pstmt.setInt(7, tenantId);
            pstmt.executeUpdate();
        }
    }

    // Method to remove a tenant
    public static void removeTenant(int tenantId) throws SQLException {
        String sql = "DELETE FROM Tenant WHERE TenantID = ?";
        try (Connection con = DBUtils.establishConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, tenantId);
            pstmt.executeUpdate();
        }
    }

}