package MainMenu;

import Menus.*;
import Users.UsersDisplay;
import Menus.Properties.*;
import Menus.Tenants.TenantsDisplay;
import Menus.Payments.*;
import Menus.Leases.*;
import Menus.Maintenances.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class DisplayOptions {
    private Stage stage;
    private String userRole;

    public DisplayOptions(Stage stage, String userRole) {
        this.stage = stage;
        this.userRole = userRole;
    }

    public void showOptions() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));

        Label title = new Label("Real Estate Property Management");
        title.setFont(new Font("Arial", 24));
        title.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");

        Button leasesButton = new Button("Leases");
        leasesButton.setOnAction(e -> new LeasesDisplay(stage, userRole).showLeases());

        Button propertiesButton = new Button("Properties");
        propertiesButton.setOnAction(e -> new PropertiesDisplay(stage, userRole).showProperties());

        Button maintenanceRequestsButton = new Button("Maintenance Requests");
        maintenanceRequestsButton.setOnAction(e -> new MaintenancesDisplay(stage, userRole).showMaintenances());

        Button paymentsButton = new Button("Payments");
        paymentsButton.setOnAction(e -> new PaymentsDisplay(stage, userRole).showPayments());

        Button tenantsButton = new Button("Tenants");
        tenantsButton.setOnAction(e -> new TenantsDisplay(stage, userRole).showTenants());

        Button signOutButton = new Button("Sign Out");
        signOutButton.setOnAction(e -> new UserLogin(stage).initializeComponents());

        layout.getChildren().addAll(title, leasesButton, propertiesButton, maintenanceRequestsButton, paymentsButton, tenantsButton);

        if ("Admin".equals(userRole)) {
            Button usersButton = new Button("Users");
            usersButton.setOnAction(e -> new UsersDisplay(stage, userRole).showUsers());
            layout.getChildren().add(usersButton);
        }

        layout.getChildren().add(signOutButton);

        Scene scene = new Scene(layout, 600, 600);
        stage.setScene(scene);
        stage.show();
    }

    public void signOut() {
        new UserLogin(stage).initializeComponents();
    }
}