// PaymentsDisplay.java

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

// Class to display payments in a table
public class PaymentsDisplay {
    // Stage on which the current scene will be shown
    private Stage stage;
    private String userRole;
    // Constructor that initializes the stage
    public PaymentsDisplay(Stage stage, String userRole) {
        this.stage = stage;
        this.userRole = userRole;
    }

    // Method to show the payments in a table
    public void showPayments() {
        // Create a TableView to display the payments
        TableView<Payment> paymentsTable = new TableView<>();

        // Define the columns for the table
        // Each column represents a field in the Payment class
        // The PropertyValueFactory uses the getter methods in the Payment class to
        // populate the columns
        TableColumn<Payment, Integer> idColumn = new TableColumn<>("Payment ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Payment, Integer> leaseIDColumn = new TableColumn<>("Lease ID");
        leaseIDColumn.setCellValueFactory(new PropertyValueFactory<>("leaseID"));

        TableColumn<Payment, Integer> tenantIDColumn = new TableColumn<>("Tenant ID");
        tenantIDColumn.setCellValueFactory(new PropertyValueFactory<>("tenantID"));

        TableColumn<Payment, String> paymentDateColumn = new TableColumn<>("Payment Date");
        paymentDateColumn.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));

        TableColumn<Payment, String> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Payment, String> paymentTypeColumn = new TableColumn<>("Payment Type");
        paymentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("paymentType"));

        TableColumn<Payment, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Payment, String> receiptNumberColumn = new TableColumn<>("Receipt Number");
        receiptNumberColumn.setCellValueFactory(new PropertyValueFactory<>("receiptNumber"));

        // Add the columns to the PaymentsTable one by one
        paymentsTable.getColumns().add(idColumn);
        paymentsTable.getColumns().add(leaseIDColumn);
        paymentsTable.getColumns().add(tenantIDColumn);
        paymentsTable.getColumns().add(paymentDateColumn);
        paymentsTable.getColumns().add(amountColumn);
        paymentsTable.getColumns().add(paymentTypeColumn);
        paymentsTable.getColumns().add(descriptionColumn);
        paymentsTable.getColumns().add(receiptNumberColumn);

        // Retrieve payments from the database
        ObservableList<Payment> payments = FXCollections.observableArrayList();
        try (Connection con = DBUtils.establishConnection();
                Statement stmt = con.createStatement()) {
            // Execute a SQL query to retrieve the payments
            ResultSet rs = stmt.executeQuery(
                    "SELECT PaymentID, LeaseID, TenantID, PaymentDate, Amount, PaymentType, Description, ReceiptNumber FROM Payments");
            while (rs.next()) {
                // Create a Payment object for each row in the result set
                Payment payment = new Payment(rs.getInt("PaymentID"), rs.getInt("LeaseID"), rs.getInt("TenantID"),
                        rs.getDate("PaymentDate"), rs.getString("Amount"), rs.getString("PaymentType"),
                        rs.getString("Description"), rs.getString("ReceiptNumber"));
                // Add the Payment object to the ObservableList
                payments.add(payment);
            }
        } catch (Exception e) {
            // Handle exceptions (e.g., SQL errors)
            e.printStackTrace();
        }

        // Set the items for the TableView
        paymentsTable.setItems(payments);

        // Create a back button to return to the previous screen
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            // Go back to the DisplayOptions screen
            DisplayOptions displayOptions = new DisplayOptions(stage, this.userRole);
            displayOptions.showOptions();
        });

        // Create the layout for the payments display
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(paymentsTable, backButton);

        // Create the scene and set it on the stage
        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    // Payment class to hold the data for each payment
    public static class Payment {
        // Fields for each column in the payments table
        private final int id;
        private final int leaseID;
        private final int tenantID;
        private final String paymentDate;
        private final String amount;
        private final String paymentType;
        private final String description;
        private final String receiptNumber;

        // Constructor that initializes the fields
        public Payment(int id, int leaseID, int tenantID, java.sql.Date paymentDate, String amount, String paymentType,
                String description, String receiptNumber) {
            this.id = id;
            this.leaseID = leaseID;
            this.tenantID = tenantID;
            this.paymentDate = paymentDate.toString();
            this.amount = amount;
            this.paymentType = paymentType;
            this.description = description;
            this.receiptNumber = receiptNumber;
        }

        // Getter methods for each field
        public int getId() {
            return id;
        }

        public int getLeaseID() {
            return leaseID;
        }

        public int getTenantID() {
            return tenantID;
        }

        public String getPaymentDate() {
            return paymentDate;
        }

        public String getAmount() {
            return amount;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public String getDescription() {
            return description;
        }

        public String getReceiptNumber() {
            return receiptNumber;
        }
    }
}
