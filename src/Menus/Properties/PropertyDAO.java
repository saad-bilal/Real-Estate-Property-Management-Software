package Menus.Properties;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Utilities.DBUtils;

public class PropertyDAO {

    // Method to retrieve all properties
    public static List<Property> getAllProperties() throws SQLException {
        List<Property> properties = new ArrayList<>();
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
        }
        return properties;
    }
    // Method to add a new property

    public static void addProperty(String type, String size, String location, String price, String furnishingStatus,
            String maintenanceHistory) throws SQLException {
        String sql = "INSERT INTO Property (Type, Size, Location, Price, FurnishingStatus, MaintenanceHistory) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBUtils.establishConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, type);
            pstmt.setString(2, size);
            pstmt.setString(3, location);
            pstmt.setString(4, price);
            pstmt.setString(5, furnishingStatus);
            pstmt.setString(6, maintenanceHistory);
            pstmt.executeUpdate();
        }
    }

    // Method to update an existing property
    public static void updateProperty(int propertyId, String type, String size, String location, String price,
            String furnishingStatus, String maintenanceHistory) throws SQLException {
        String sql = "UPDATE Property SET Type = ?, Size = ?, Location = ?, Price = ?, FurnishingStatus = ?, MaintenanceHistory = ? WHERE PropertyID = ?";
        try (Connection con = DBUtils.establishConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, type);
            pstmt.setString(2, size);
            pstmt.setString(3, location);
            pstmt.setString(4, price);
            pstmt.setString(5, furnishingStatus);
            pstmt.setString(6, maintenanceHistory);
            pstmt.setInt(7, propertyId);
            pstmt.executeUpdate();
        }
    }

    // Method to remove a property
    public static void removeProperty(int propertyId) throws SQLException {
        String sql = "DELETE FROM Property WHERE PropertyID = ?";
        try (Connection con = DBUtils.establishConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, propertyId);
            pstmt.executeUpdate();
        }
    }

}