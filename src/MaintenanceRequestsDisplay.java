// MaintenanceRequestsDisplay.java

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

// Class to display maintenance requests in a table
public class MaintenanceRequestsDisplay {
    // Stage on which the current scene will be shown
    private Stage stage;
    private String userRole;

    // Constructor that initializes the stage
    public MaintenanceRequestsDisplay(Stage stage, String userRole) {
        this.stage = stage;
        this.userRole = userRole;
    }

    // Method to show the maintenance requests in a table
    public void showMaintenanceRequests() {
        // Create a TableView to display the maintenance requests
        TableView<MaintenanceRequest> maintenanceRequestsTable = new TableView<>();

        // Define the columns for the table
        // Each column represents a field in the MaintenanceRequest class
        // The PropertyValueFactory uses the getter methods in the MaintenanceRequest
        // class to populate the columns
        TableColumn<MaintenanceRequest, Integer> idColumn = new TableColumn<>("Request ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<MaintenanceRequest, Integer> propertyIDColumn = new TableColumn<>("Property ID");
        propertyIDColumn.setCellValueFactory(new PropertyValueFactory<>("propertyID"));

        TableColumn<MaintenanceRequest, Integer> tenantIDColumn = new TableColumn<>("Tenant ID");
        tenantIDColumn.setCellValueFactory(new PropertyValueFactory<>("tenantID"));

        TableColumn<MaintenanceRequest, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<MaintenanceRequest, String> reportDateColumn = new TableColumn<>("Report Date");
        reportDateColumn.setCellValueFactory(new PropertyValueFactory<>("reportDate"));

        TableColumn<MaintenanceRequest, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<MaintenanceRequest, String> priorityColumn = new TableColumn<>("Priority");
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));

        TableColumn<MaintenanceRequest, String> resolutionDateColumn = new TableColumn<>("Resolution Date");
        resolutionDateColumn.setCellValueFactory(new PropertyValueFactory<>("resolutionDate"));

        // Add the columns to the MaintenanceRequestsTable one by one
        maintenanceRequestsTable.getColumns().add(idColumn);
        maintenanceRequestsTable.getColumns().add(propertyIDColumn);
        maintenanceRequestsTable.getColumns().add(tenantIDColumn);
        maintenanceRequestsTable.getColumns().add(descriptionColumn);
        maintenanceRequestsTable.getColumns().add(reportDateColumn);
        maintenanceRequestsTable.getColumns().add(statusColumn);
        maintenanceRequestsTable.getColumns().add(priorityColumn);
        maintenanceRequestsTable.getColumns().add(resolutionDateColumn);

        // Retrieve maintenance requests from the database
        ObservableList<MaintenanceRequest> maintenanceRequests = FXCollections.observableArrayList();
        try (Connection con = DBUtils.establishConnection();
                Statement stmt = con.createStatement()) {
            // Execute a SQL query to retrieve the maintenance requests
            ResultSet rs = stmt.executeQuery(
                    "SELECT RequestID, PropertyID, TenantID, Description, ReportDate, Status, Priority, ResolutionDate FROM MaintenanceRequests");
            while (rs.next()) {
                // Create a MaintenanceRequest object for each row in the result set
                MaintenanceRequest maintenanceRequest = new MaintenanceRequest(rs.getInt("RequestID"),
                        rs.getInt("PropertyID"), rs.getInt("TenantID"), rs.getString("Description"),
                        rs.getDate("ReportDate"), rs.getString("Status"), rs.getString("Priority"),
                        rs.getDate("ResolutionDate"));
                // Add the MaintenanceRequest object to the ObservableList
                maintenanceRequests.add(maintenanceRequest);
            }
        } catch (Exception e) {
            // Handle exceptions (e.g., SQL errors)
            e.printStackTrace();
        }

        // Set the items for the TableView
        maintenanceRequestsTable.setItems(maintenanceRequests);

        // Create a back button to return to the previous screen
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            // Go back to the DisplayOptions screen
            DisplayOptions displayOptions = new DisplayOptions(stage, this.userRole);
            displayOptions.showOptions();
        });

        // Create the layout for the maintenance requests display
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(maintenanceRequestsTable, backButton);

        // Create the scene and set it on the stage
        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    // MaintenanceRequest class to hold the data for each maintenance request
    public static class MaintenanceRequest {
        // Fields for each column in the maintenance requests table
        private final int id;
        private final int propertyID;
        private final int tenantID;
        private final String description;
        private final String reportDate;
        private final String status;
        private final String priority;
        private final String resolutionDate;

        // Constructor that initializes the fields
        public MaintenanceRequest(int id, int propertyID, int tenantID, String description, java.sql.Date reportDate,
                String status, String priority, java.sql.Date resolutionDate) {
            this.id = id;
            this.propertyID = propertyID;
            this.tenantID = tenantID;
            this.description = description;
            this.reportDate = reportDate.toString();
            this.status = status;
            this.priority = priority;
            this.resolutionDate = resolutionDate != null ? resolutionDate.toString() : "";
        }

        // Getter methods for each field
        public int getId() {
            return id;
        }

        public int getPropertyID() {
            return propertyID;
        }

        public int getTenantID() {
            return tenantID;
        }

        public String getDescription() {
            return description;
        }

        public String getReportDate() {
            return reportDate;
        }

        public String getStatus() {
            return status;
        }

        public String getPriority() {
            return priority;
        }

        public String getResolutionDate() {
            return resolutionDate;
        }
    }
}
