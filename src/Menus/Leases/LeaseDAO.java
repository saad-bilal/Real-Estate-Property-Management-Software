package Menus.Leases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Utilities.DBUtils;
import java.sql.Statement;

public class LeaseDAO {

    // Method to retrieve all lease agreements
    public static List<Lease> getAllLeaseAgreements() throws SQLException {
        List<Lease> leaseAgreements = new ArrayList<>();
        String query = "SELECT LeaseID, PropertyID, TenantID, StartDate, EndDate, MonthlyRent, SecurityDeposit, SignatureDate, Status FROM leaseagreements";

        try (Connection con = DBUtils.establishConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Lease lease = new Lease(rs.getInt("LeaseID"), rs.getInt("PropertyID"),
                        rs.getInt("TenantID"),
                        rs.getDate("StartDate").toString(), rs.getDate("EndDate").toString(),
                        rs.getString("MonthlyRent"),
                        rs.getString("SecurityDeposit"),
                        rs.getDate("SignatureDate").toString(), rs.getString("Status"));
                leaseAgreements.add(lease);
            }
        }
        return leaseAgreements;
    }

    // Method to add a new lease agreement
    public static void addLeaseAgreement(int propertyId, int tenantId, String startDate, String endDate,
            String monthlyRent, String securityDeposit, String signatureDate, String status) throws SQLException {
        String sql = "INSERT INTO leaseagreements (PropertyID, TenantID, StartDate, EndDate, MonthlyRent, SecurityDeposit, SignatureDate, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBUtils.establishConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, propertyId);
            pstmt.setInt(2, tenantId);
            pstmt.setDate(3, java.sql.Date.valueOf(startDate));
            pstmt.setDate(4, java.sql.Date.valueOf(endDate));
            pstmt.setString(5, monthlyRent);
            pstmt.setString(6, securityDeposit);
            pstmt.setDate(7, java.sql.Date.valueOf(signatureDate));
            pstmt.setString(8, status);
            pstmt.executeUpdate();
        }
    }

    // Method to update an existing lease agreement
    public static void updateLeaseAgreement(int leaseId, int propertyId, int tenantId, String startDate,
            String endDate, String monthlyRent, String securityDeposit, String signatureDate, String status)
            throws SQLException {
        String sql = "UPDATE leaseagreements SET PropertyID = ?, TenantID = ?, StartDate = ?, EndDate = ?, MonthlyRent = ?, SecurityDeposit = ?, SignatureDate = ?, Status = ? WHERE LeaseID = ?";
        try (Connection con = DBUtils.establishConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, propertyId);
            pstmt.setInt(2, tenantId);
            pstmt.setDate(3, java.sql.Date.valueOf(startDate));
            pstmt.setDate(4, java.sql.Date.valueOf(endDate));
            pstmt.setString(5, monthlyRent);
            pstmt.setString(6, securityDeposit);
            pstmt.setDate(7, java.sql.Date.valueOf(signatureDate));
            pstmt.setString(8, status);
            pstmt.setInt(9, leaseId);
            pstmt.executeUpdate();
        }
    }

    // Method to remove a lease agreement
    public static void removeLeaseAgreement(int leaseId) throws SQLException {
        String sql = "DELETE FROM leaseagreements WHERE LeaseID = ?";
        try (Connection con = DBUtils.establishConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, leaseId);
            pstmt.executeUpdate();
        }
    }
}