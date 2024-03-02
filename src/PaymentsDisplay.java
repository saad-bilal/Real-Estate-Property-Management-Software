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

public class PaymentsDisplay {
    private Stage stage;

    public PaymentsDisplay(Stage stage) {
        this.stage = stage;
    }

    public void showPayments() {
        // Create a TableView to display the payments
        TableView<Payment> paymentsTable = new TableView<>();

        // Define the columns
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

        // Add more columns as needed for other payment details

        // Add the columns to the TableView
        paymentsTable.getColumns().addAll(idColumn, leaseIDColumn, tenantIDColumn, paymentDateColumn, amountColumn, paymentTypeColumn, descriptionColumn, receiptNumberColumn);


        // Retrieve payments from the database
        ObservableList<Payment> payments = FXCollections.observableArrayList();
        try (Connection con = DBUtils.establishConnection();
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT PaymentID, LeaseID, TenantID, PaymentDate, Amount, PaymentType, Description, ReceiptNumber FROM Payments");
            while (rs.next()) {
                Payment payment = new Payment(rs.getInt("PaymentID"), rs.getInt("LeaseID"), rs.getInt("TenantID"), rs.getDate("PaymentDate"), rs.getString("Amount"), rs.getString("PaymentType"), rs.getString("Description"), rs.getString("ReceiptNumber"));
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
            DisplayOptions displayOptions = new DisplayOptions(stage);
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
        private final int id;
        private final int leaseID;
        private final int tenantID;
        private final String paymentDate;
        private final String amount;
        private final String paymentType;
        private final String description;
        private final String receiptNumber;

        public Payment(int id, int leaseID, int tenantID, java.sql.Date paymentDate, String amount, String paymentType, String description, String receiptNumber) {
            this.id = id;
            this.leaseID = leaseID;
            this.tenantID = tenantID;
            this.paymentDate = paymentDate.toString();
            this.amount = amount;
            this.paymentType = paymentType;
            this.description = description;
            this.receiptNumber = receiptNumber;
        }

        public int getId() { return id; }
        public int getLeaseID() { return leaseID; }
        public int getTenantID() { return tenantID; }
        public String getPaymentDate() { return paymentDate; }
        public String getAmount() { return amount; }
        public String getPaymentType() { return paymentType; }
        public String getDescription() { return description; }
        public String getReceiptNumber() { return receiptNumber; }
    }
}