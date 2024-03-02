// Import necessary JavaFX and SQL classes
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

// Class to display leases in a table
public class LeasesDisplay {
    // Stage on which the current scene will be shown
    private Stage stage;

    // Constructor that initializes the stage
    public LeasesDisplay(Stage stage) {
        this.stage = stage;
    }

    // Method to show the leases in a table
    public void showLeases() {
        // Create a TableView to display the leases
        TableView<Lease> leasesTable = new TableView<>();

        // Define the columns for the table
        // Each column represents a field in the Lease class
        // The PropertyValueFactory uses the getter methods in the Lease class to populate the columns
        TableColumn<Lease, Integer> idColumn = new TableColumn<>("Lease ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Lease, Integer> propertyIDColumn = new TableColumn<>("Property ID");
        propertyIDColumn.setCellValueFactory(new PropertyValueFactory<>("propertyID"));

        TableColumn<Lease, Integer> tenantIDColumn = new TableColumn<>("Tenant ID");
        tenantIDColumn.setCellValueFactory(new PropertyValueFactory<>("tenantID"));

        TableColumn<Lease, String> startDateColumn = new TableColumn<>("Start Date");
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        TableColumn<Lease, String> endDateColumn = new TableColumn<>("End Date");
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        TableColumn<Lease, String> monthlyRentColumn = new TableColumn<>("Monthly Rent");
        monthlyRentColumn.setCellValueFactory(new PropertyValueFactory<>("monthlyRent"));

        TableColumn<Lease, String> securityDepositColumn = new TableColumn<>("Security Deposit");
        securityDepositColumn.setCellValueFactory(new PropertyValueFactory<>("securityDeposit"));

        TableColumn<Lease, String> signatureDateColumn = new TableColumn<>("Signature Date");
        signatureDateColumn.setCellValueFactory(new PropertyValueFactory<>("signatureDate"));

        TableColumn<Lease, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Add the columns to the TableView
        leasesTable.getColumns().addAll(idColumn, propertyIDColumn, tenantIDColumn, startDateColumn, endDateColumn, monthlyRentColumn, securityDepositColumn, signatureDateColumn, statusColumn);

        // Retrieve leases from the database
        ObservableList<Lease> leases = FXCollections.observableArrayList();
        try (Connection con = DBUtils.establishConnection();
             Statement stmt = con.createStatement()) {
            // Execute a SQL query to retrieve the leases
            ResultSet rs = stmt.executeQuery("SELECT LeaseID, PropertyID, TenantID, StartDate, EndDate, MonthlyRent, SecurityDeposit, SignatureDate, Status FROM LeaseAgreements");
            while (rs.next()) {
                // Create a Lease object for each row in the result set
                Lease lease = new Lease(rs.getInt("LeaseID"), rs.getInt("PropertyID"), rs.getInt("TenantID"), rs.getDate("StartDate"), rs.getDate("EndDate"), rs.getString("MonthlyRent"), rs.getString("SecurityDeposit"), rs.getDate("SignatureDate"), rs.getString("Status"));
                // Add the Lease object to the ObservableList
                leases.add(lease);
            }
        } catch (Exception e) {
            // Handle exceptions (e.g., SQL errors)
            e.printStackTrace();
        }

        // Set the items for the TableView
        leasesTable.setItems(leases);

        // Create a back button to return to the previous screen
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            // Go back to the DisplayOptions screen
            DisplayOptions displayOptions = new DisplayOptions(stage);
            displayOptions.showOptions();
        });

        // Create the layout for the leases display
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(leasesTable, backButton);

        // Create the scene and set it on the stage
        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    // Lease class to hold the data for each lease
    public static class Lease {
        // Fields for each column in the leases table
        private final int id;
        private final int propertyID;
        private final int tenantID;
        private final String startDate;
        private final String endDate;
        private final String monthlyRent;
        private final String securityDeposit;
        private final String signatureDate;
        private final String status;

        // Constructor that initializes the fields
        public Lease(int id, int propertyID, int tenantID, java.sql.Date startDate, java.sql.Date endDate, String monthlyRent, String securityDeposit, java.sql.Date signatureDate, String status) {
            this.id = id;
            this.propertyID = propertyID;
            this.tenantID = tenantID;
            this.startDate = startDate.toString();
            this.endDate = endDate != null ? endDate.toString() : "";
            this.monthlyRent = monthlyRent;
            this.securityDeposit = securityDeposit;
            this.signatureDate = signatureDate.toString();
            this.status = status;
        }

        // Getter methods for each field
        public int getId() { return id; }
        public int getPropertyID() { return propertyID; }
        public int getTenantID() { return tenantID; }
        public String getStartDate() { return startDate; }
        public String getEndDate() { return endDate; }
        public String getMonthlyRent() { return monthlyRent; }
        public String getSecurityDeposit() { return securityDeposit; }
        public String getSignatureDate() { return signatureDate; }
        public String getStatus() { return status; }
    }
}