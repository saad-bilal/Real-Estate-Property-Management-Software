
// Import necessary JavaFX classes
// import java.sql.Connection;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

// This class represents the display options in the application
public class DisplayOptions {
    // Stage on which the current scene will be shown
    private Stage stage;

    // Constructor that initializes the stage
    public DisplayOptions(Stage stage) {
        this.stage = stage;
    }

    // private String currentUsername;

    // public void setCurrentUsername(String username) {
    //     this.currentUsername = username;
    // }

    // private String getUserRole() {
    //     String role = "Employee"; // Default role if not found
    //     try (Connection con = DBUtils.establishConnection();
    //             PreparedStatement pstmt = con.prepareStatement("SELECT Role FROM Users WHERE Username = ?")) {
    //         pstmt.setString(1, currentUsername);
    //         ResultSet rs = pstmt.executeQuery();
    //         if (rs.next()) {
    //             role = rs.getString("Role");
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    //     return role;
    // }

    private String userRole; // Add this line to store user role

    // Modify the constructor to accept user role
    public DisplayOptions(Stage stage, String userRole) {
        this.stage = stage;
        this.userRole = userRole;
    }

    // Method to show the options to the user
    public void showOptions() {
        // Create a VBox layout for the options
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setSpacing(10);

        // Set a custom font for the title
        Label title = new Label("Real Estate Property Management");
        title.setFont(new Font("Arial", 24));
        title.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");

        // Create buttons for each option
        Button leasesButton = new Button("Leases");
        leasesButton.setOnAction(e -> {
            LeasesDisplay leasesDisplay = new LeasesDisplay(stage);
            leasesDisplay.showLeases();
        });

        Button propertiesButton = new Button("Properties");
        propertiesButton.setOnAction(e -> {
            PropertiesDisplay propertiesDisplay = new PropertiesDisplay(stage);
            propertiesDisplay.showProperties();
        });

        Button maintenanceRequestsButton = new Button("Maintenance Requests");
        maintenanceRequestsButton.setOnAction(e -> {
            MaintenanceRequestsDisplay maintenanceRequestsDisplay = new MaintenanceRequestsDisplay(stage);
            maintenanceRequestsDisplay.showMaintenanceRequests();
        });

        Button paymentsButton = new Button("Payments");
        paymentsButton.setOnAction(e -> {
            PaymentsDisplay paymentsDisplay = new PaymentsDisplay(stage);
            paymentsDisplay.showPayments();
        });

        Button tenantsButton = new Button("Tenants");
        tenantsButton.setOnAction(e -> {
            TenantsDisplay tenantsDisplay = new TenantsDisplay(stage);
            tenantsDisplay.showTenants();
        });

        Button signOutButton = new Button("Sign Out");
        signOutButton.setOnAction(e -> {
            UserLogin login = new UserLogin(stage);
            login.initializeComponents();
        }); // Sign out button action

        // Add the buttons to the layout
        layout.getChildren().addAll(title, leasesButton, propertiesButton, maintenanceRequestsButton, paymentsButton,
                tenantsButton);

        // Only add the Users button if the user is an admin
        if ("Admin".equals(this.userRole)) {
            Button usersButton = new Button("Users");
            usersButton.setOnAction(e -> {
                UsersDisplay usersDisplay = new UsersDisplay(stage);
                usersDisplay.showUsers();
            });
            layout.getChildren().add(usersButton);
        }

        // Add the sign out button at the end
        layout.getChildren().add(signOutButton);

        // Create the scene and set it on the stage
        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    // Method to sign out and return to the login screen
    public void signOut() {

        // Reset the scene to the login screen
        UserLogin login = new UserLogin(stage);
        login.initializeComponents();
    }
}