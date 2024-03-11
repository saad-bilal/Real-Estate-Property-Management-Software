// DBUtils.java

import java.sql.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

// This class provides utility methods for database operations
public class DBUtils {
    // Database connection details
    private static String url;
    private static String appUsername;
    private static String appPassword;

    // Load environment variables when the class is loaded
    static {
        loadEnvVariables();
    }

    // Method to load environment variables from .env file
    private static void loadEnvVariables() {
        // Map to store environment variables
        Map<String, String> envMap = new HashMap<>();
        try {
            // Read the .env file line by line
            Files.lines(Paths.get(".env")).forEach(line -> {
                // Split each line into key and value
                String[] parts = line.split("=", 2);
                if (parts.length >= 2) {
                    // Add the key-value pair to the map
                    envMap.put(parts[0].trim(), parts[1].trim());
                }
            });
        } catch (IOException e) {
            // Throw an exception if there's an error reading the .env file
            throw new RuntimeException("Error loading .env file: " + e.getMessage(), e);
        }

        // Get the database connection details from the map
        url = envMap.get("DB_URL");
        appUsername = envMap.get("DB_USERNAME");
        appPassword = envMap.get("DB_PASSWORD");
    }

    // Method to establish a database connection
    public static Connection establishConnection() {
        try {
            // Get a connection to the database
            Connection con = DriverManager.getConnection(url, appUsername, appPassword);
            System.out.println("Connection Successful");
            return con;
        } catch (SQLException e) {
            // Throw an exception if there's an error connecting to the database
            throw new RuntimeException("Failed to establish database connection: " + e.getMessage(), e);
        }
    }

    // Method to close a database connection and a statement
    public static void closeConnection(Connection con, Statement stmt) {
        try {
            // Close the statement if it's not null
            if (stmt != null)
                stmt.close();
        } catch (SQLException e) {
            System.err.println("Failed to close Statement: " + e.getMessage());
        } finally {
            try {
                // Close the connection if it's not null
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                System.err.println("Failed to close Connection: " + e.getMessage());
            }
        }
        System.out.println("Connection is closed");
    }

    // Method to close a PreparedStatement
    public static void closePreparedStatement(PreparedStatement pstmt) {
        try {
            // Close the PreparedStatement if it's not null
            if (pstmt != null)
                pstmt.close();
        } catch (SQLException e) {
            System.err.println("Failed to close PreparedStatement: " + e.getMessage());
        }
    }
}
