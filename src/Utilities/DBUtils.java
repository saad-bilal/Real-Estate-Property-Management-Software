package Utilities;

import java.sql.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class DBUtils {
    private static String url;
    private static String appUsername;
    private static String appPassword;

    static {
        loadEnvVariables();
    }

    private static void loadEnvVariables() {
        Map<String, String> envMap = new HashMap<>();
        try {
            Files.lines(Paths.get(".env")).forEach(line -> {
                String[] parts = line.split("=", 2);
                if (parts.length >= 2) {
                    envMap.put(parts[0].trim(), parts[1].trim());
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Error loading .env file: " + e.getMessage(), e);
        }

        url = envMap.get("DB_URL");
        appUsername = envMap.get("DB_USERNAME");
        appPassword = envMap.get("DB_PASSWORD");
    }

    public static Connection establishConnection() {
        try {
            Connection con = DriverManager.getConnection(url, appUsername, appPassword);
            System.out.println("Connection Successful");
            return con;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to establish database connection: " + e.getMessage(), e);
        }
    }

    public static void closeConnection(Connection con, Statement stmt) {
        try {
            if (stmt != null)
                stmt.close();
        } catch (SQLException e) {
            System.err.println("Failed to close Statement: " + e.getMessage());
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                System.err.println("Failed to close Connection: " + e.getMessage());
            }
        }
        System.out.println("Connection is closed");
    }

    public static void closePreparedStatement(PreparedStatement pstmt) {
        try {
            if (pstmt != null)
                pstmt.close();
        } catch (SQLException e) {
            System.err.println("Failed to close PreparedStatement: " + e.getMessage());
        }
    }
}