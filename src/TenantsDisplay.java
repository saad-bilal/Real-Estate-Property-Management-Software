// TenansDisplay.java

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

// Class to display tenants in a table
public class TenantsDisplay {
    // Stage on which the current scene will be shown
    private Stage stage;
    private String userRole;

    // Constructor that initializes the stage
    public TenantsDisplay(Stage stage, String userRole) {
        this.stage = stage;
        this.userRole = userRole;
    }

    // Method to show the tenants in a table
    public void showTenants() {
        // Create a TableView to display the tenants
        TableView<Tenant> tenantsTable = new TableView<>();

        // Define the columns for the table
        // Each column represents a field in the Tenant class
        // The PropertyValueFactory uses the getter methods in the Tenant class to
        // populate the columns
        TableColumn<Tenant, Integer> idColumn = new TableColumn<>("Tenant ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Tenant, String> nameColumn = new TableColumn<>("Tenant Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Tenant, String> emailColumn = new TableColumn<>("Email Address");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Tenant, String> phoneNumberColumn = new TableColumn<>("Phone Number");
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        TableColumn<Tenant, String> moveInDateColumn = new TableColumn<>("Move-in Date");
        moveInDateColumn.setCellValueFactory(new PropertyValueFactory<>("moveInDate"));

        TableColumn<Tenant, String> moveOutDateColumn = new TableColumn<>("Move-out Date");
        moveOutDateColumn.setCellValueFactory(new PropertyValueFactory<>("moveOutDate"));

        // Add the columns to the TenantsTable one by one
        tenantsTable.getColumns().add(idColumn);
        tenantsTable.getColumns().add(nameColumn);
        tenantsTable.getColumns().add(emailColumn);
        tenantsTable.getColumns().add(phoneNumberColumn);
        tenantsTable.getColumns().add(moveInDateColumn);
        tenantsTable.getColumns().add(moveOutDateColumn);

        // Retrieve tenants from the database
        ObservableList<Tenant> tenants = FXCollections.observableArrayList();
        try (Connection con = DBUtils.establishConnection();
                Statement stmt = con.createStatement()) {
            // Execute a SQL query to retrieve the tenants
            ResultSet rs = stmt.executeQuery(
                    "SELECT TenantID, TenantName, EmailAddress, PhoneNumber, MoveInDate, MoveOutDate FROM Tenant");
            while (rs.next()) {
                // Create a Tenant object for each row in the result set
                Tenant tenant = new Tenant(rs.getInt("TenantID"), rs.getString("TenantName"),
                        rs.getString("EmailAddress"), rs.getString("PhoneNumber"), rs.getDate("MoveInDate"),
                        rs.getDate("MoveOutDate"));
                // Add the Tenant object to the ObservableList
                tenants.add(tenant);
            }
        } catch (Exception e) {
            // Handle exceptions (e.g., SQL errors)
            e.printStackTrace();
        }

        // Set the items for the TableView
        tenantsTable.setItems(tenants);

        // Create a back button to return to the previous screen
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            // Go back to the DisplayOptions screen
            DisplayOptions displayOptions = new DisplayOptions(stage, this.userRole);
            displayOptions.showOptions();
        });

        // Create the layout for the tenants display
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(tenantsTable, backButton);

        // Create the scene and set it on the stage
        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    // Tenant class to hold the data for each tenant
    public static class Tenant {
        // Fields for each column in the tenants table
        private final int id;
        private final String name;
        private final String email;
        private final String phoneNumber;
        private final String moveInDate;
        private final String moveOutDate;

        // Constructor that initializes the fields
        public Tenant(int id, String name, String email, String phoneNumber, java.sql.Date moveInDate,
                java.sql.Date moveOutDate) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.moveInDate = moveInDate.toString();
            this.moveOutDate = moveOutDate != null ? moveOutDate.toString() : "";
        }

        // Getter methods for each field
        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getMoveInDate() {
            return moveInDate;
        }

        public String getMoveOutDate() {
            return moveOutDate;
        }
    }
}
