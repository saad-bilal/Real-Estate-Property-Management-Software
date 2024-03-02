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

public class TenantsDisplay {
    private Stage stage;

    public TenantsDisplay(Stage stage) {
        this.stage = stage;
    }

    public void showTenants() {
        // Create a TableView to display the tenants
        TableView<Tenant> tenantsTable = new TableView<>();

        // Define the columns
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

        // Add the columns to the TableView
        tenantsTable.getColumns().addAll(idColumn, nameColumn, emailColumn, phoneNumberColumn, moveInDateColumn, moveOutDateColumn);

        // Retrieve tenants from the database
        ObservableList<Tenant> tenants = FXCollections.observableArrayList();
        try (Connection con = DBUtils.establishConnection();
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT TenantID, TenantName, EmailAddress, PhoneNumber, MoveInDate, MoveOutDate FROM Tenant");
            while (rs.next()) {
                Tenant tenant = new Tenant(rs.getInt("TenantID"), rs.getString("TenantName"), rs.getString("EmailAddress"), rs.getString("PhoneNumber"), rs.getDate("MoveInDate"), rs.getDate("MoveOutDate"));
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
            DisplayOptions displayOptions = new DisplayOptions(stage);
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
        private final int id;
        private final String name;
        private final String email;
        private final String phoneNumber;
        private final String moveInDate;
        private final String moveOutDate;

        public Tenant(int id, String name, String email, String phoneNumber, java.sql.Date moveInDate, java.sql.Date moveOutDate) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.moveInDate = moveInDate.toString();
            this.moveOutDate = moveOutDate != null ? moveOutDate.toString() : "";
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getPhoneNumber() { return phoneNumber; }
        public String getMoveInDate() { return moveInDate; }
        public String getMoveOutDate() { return moveOutDate; }
    }
}