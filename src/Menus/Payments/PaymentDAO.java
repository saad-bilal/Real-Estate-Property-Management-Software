package Menus.Payments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Utilities.DBUtils;
import java.sql.Statement;

public class PaymentDAO {

    // Method to retrieve all payments
    public static List<Payment> getAllPayments() throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String query = "SELECT PaymentID, LeaseID, TenantID, PaymentDate, Amount, PaymentType, Description, ReceiptNumber FROM Payments";

        try (Connection con = DBUtils.establishConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Payment payment = new Payment(rs.getInt("PaymentID"), rs.getInt("LeaseID"), rs.getInt("TenantID"),
                        rs.getDate("PaymentDate").toString(), rs.getString("Amount"), rs.getString("PaymentType"),
                        rs.getString("Description"), rs.getString("ReceiptNumber"));
                payments.add(payment);
            }
        }
        return payments;
    }

    // Method to add a new payment
    public static void addPayment(int leaseId, int tenantId, String paymentDate, String amount, String paymentType,
            String description, String receiptNumber) throws SQLException {
        String sql = "INSERT INTO Payments (LeaseID, TenantID, PaymentDate, Amount, PaymentType, Description, ReceiptNumber) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBUtils.establishConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, leaseId);
            pstmt.setInt(2, tenantId);
            pstmt.setDate(3, java.sql.Date.valueOf(paymentDate));
            pstmt.setString(4, amount);
            pstmt.setString(5, paymentType);
            pstmt.setString(6, description);
            pstmt.setString(7, receiptNumber);
            pstmt.executeUpdate();
        }
    }

    // Method to update an existing payment
    public static void updatePayment(int paymentId, int leaseId, int tenantId, String paymentDate, String amount,
            String paymentType, String description, String receiptNumber) throws SQLException {
        String sql = "UPDATE Payments SET LeaseID = ?, TenantID = ?, PaymentDate = ?, Amount = ?, PaymentType = ?, Description = ?, ReceiptNumber = ? WHERE PaymentID = ?";
        try (Connection con = DBUtils.establishConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, leaseId);
            pstmt.setInt(2, tenantId);
            pstmt.setDate(3, java.sql.Date.valueOf(paymentDate));
            pstmt.setString(4, amount);
            pstmt.setString(5, paymentType);
            pstmt.setString(6, description);
            pstmt.setString(7, receiptNumber);
            pstmt.setInt(8, paymentId);
            pstmt.executeUpdate();
        }
    }

    // Method to remove a payment
    public static void removePayment(int paymentId) throws SQLException {
        String sql = "DELETE FROM Payments WHERE PaymentID = ?";
        try (Connection con = DBUtils.establishConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, paymentId);
            pstmt.executeUpdate();
        }
    }

}