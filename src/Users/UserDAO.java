package Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Utilities.DBUtils;
import java.sql.Statement;

public class UserDAO {

    // Method to retrieve all users
    public static List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
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
        }
        return users;
    }

    // Method to add a new user
    public static void addUser(String firstName, String lastName, String username, String password, String email,
            String phoneNumber, String role) throws SQLException {
        String sql = "INSERT INTO Users (FirstName, LastName, Username, Password, Email, PhoneNumber, Role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBUtils.establishConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, username);
            pstmt.setString(4, password);
            pstmt.setString(5, email);
            pstmt.setString(6, phoneNumber);
            pstmt.setString(7, role);
            pstmt.executeUpdate();
        }
    }

    // Method to update an existing user
    public static void updateUser(int userId, String firstName, String lastName, String username, String email,
            String phoneNumber, String role) throws SQLException {
        String sql = "UPDATE Users SET FirstName = ?, LastName = ?, Username = ?, Email = ?, PhoneNumber = ?, Role = ? WHERE UserID = ?";
        try (Connection con = DBUtils.establishConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, username);
            pstmt.setString(4, email);
            pstmt.setString(5, phoneNumber);
            pstmt.setString(6, role);
            pstmt.setInt(7, userId);
            pstmt.executeUpdate();
        }
    }

    // Method to remove a user
    public static void removeUser(int userId) throws SQLException {
        String sql = "DELETE FROM Users WHERE UserID = ?";
        try (Connection con = DBUtils.establishConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        }
    }
}