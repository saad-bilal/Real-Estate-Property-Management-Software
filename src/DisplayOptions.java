// Import necessary JavaFX classes
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
        Button propertiesButton = new Button("Properties");
        Button maintenanceRequestsButton = new Button("Maintenance Requests");
        Button paymentsButton = new Button("Payments");
        Button tenantsButton = new Button("Tenants");
        Button usersButton = new Button("Users");

        // Add action handlers for each button
        leasesButton.setOnAction(e -> {
            // Navigate to the Leases display
            LeasesDisplay leasesDisplay = new LeasesDisplay(stage);
            leasesDisplay.showLeases();
        });
        propertiesButton.setOnAction(e -> {
            // Navigate to the Properties display
            PropertiesDisplay propertiesDisplay = new PropertiesDisplay(stage);
            propertiesDisplay.showProperties();
        });
        maintenanceRequestsButton.setOnAction(e -> {
            // Navigate to the Maintenance Requests display
            MaintenanceRequestsDisplay maintenanceRequestsDisplay = new MaintenanceRequestsDisplay(stage);
            maintenanceRequestsDisplay.showMaintenanceRequests();
        });
        paymentsButton.setOnAction(e -> {
            // Navigate to the Payments display
            PaymentsDisplay paymentsDisplay = new PaymentsDisplay(stage);
            paymentsDisplay.showPayments();
        });
        tenantsButton.setOnAction(e -> {
            // Navigate to the Tenants display
            TenantsDisplay tenantsDisplay = new TenantsDisplay(stage);
            tenantsDisplay.showTenants();
        });
        usersButton.setOnAction(e -> {
            // Navigate to the Users display
            UsersDisplay usersDisplay = new UsersDisplay(stage);
            usersDisplay.showUsers();
        });

        // Add the buttons to the layout
        layout.getChildren().addAll(title, leasesButton, propertiesButton, maintenanceRequestsButton, paymentsButton, tenantsButton, usersButton);

        // Create the scene and set it on the stage
        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
}