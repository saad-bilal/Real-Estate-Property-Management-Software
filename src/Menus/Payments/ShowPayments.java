package Menus.Payments;

import Utilities.*;
import MainMenu.DisplayOptions;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ShowPayments {
    private final Stage stage;
    private final String userRole;

    public ShowPayments(Stage stage, String userRole) {
        this.stage = stage;
        this.userRole = userRole;
    }

    public void display() {
        TableView<Payment> paymentsTable = new TableView<>();
        paymentsTable.setEditable(true);

        // Define the columns for the payments table
        TableColumn<Payment, Integer> idColumn = new TableColumn<>("Payment ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Payment, Integer> leaseIdColumn = new TableColumn<>("Lease ID");
        leaseIdColumn.setCellValueFactory(new PropertyValueFactory<>("leaseId"));
        TableColumn<Payment, Integer> tenantIdColumn = new TableColumn<>("Tenant ID");
        tenantIdColumn.setCellValueFactory(new PropertyValueFactory<>("tenantId"));
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

        paymentsTable.getColumns().add(idColumn);
        paymentsTable.getColumns().add(leaseIdColumn);
        paymentsTable.getColumns().add(tenantIdColumn);
        paymentsTable.getColumns().add(paymentDateColumn);
        paymentsTable.getColumns().add(amountColumn);
        paymentsTable.getColumns().add(paymentTypeColumn);
        paymentsTable.getColumns().add(descriptionColumn);
        paymentsTable.getColumns().add(receiptNumberColumn);

        ObservableList<Payment> payments = FXCollections.observableArrayList();
        try (Connection con = DBUtils.establishConnection();
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt
                    .executeQuery("SELECT PaymentID, LeaseID, TenantID, PaymentDate, Amount, PaymentType, Description, ReceiptNumber FROM Payments");
                    while (rs.next()) {
                        payments.add(new Payment(rs.getInt("PaymentID"), rs.getInt("LeaseID"), rs.getInt("TenantID"),
                                rs.getDate("PaymentDate").toString(), rs.getString("Amount"), rs.getString("PaymentType"),
                                rs.getString("Description"), rs.getString("ReceiptNumber")));
                    }
        } catch (Exception e) {
            ShowAlert.display("Database Error", "Failed to load payments: " + e.getMessage(),
                    Alert.AlertType.WARNING);
        }

        paymentsTable.setItems(payments);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            DisplayOptions displayOptions = new DisplayOptions(stage, this.userRole);
            displayOptions.showOptions();
        });

        Button addButton = new Button("Add New Payment");
        addButton.setOnAction(e -> new ShowAddPaymentForm(stage, userRole).display());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(paymentsTable, addButton, backButton);

        Scene scene = new Scene(layout, 900, 400);
        stage.setScene(scene);
        stage.show();

        TableColumn<Payment, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(column -> new TableCell<Payment, Void>() {
            private final Button editButton = new Button("Edit");
            private final Button removeButton = new Button("Remove");
            private final HBox pane = new HBox(editButton, removeButton);

            {
                pane.setSpacing(5);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);

                    Payment payment = getTableView().getItems().get(getIndex());

                    editButton.setOnAction(event -> {
                        new ShowEditPaymentForm(stage, payment, userRole).display();
                    });

                    removeButton.setOnAction(event -> {
                        RemovePayment.removePayment(payment.getId());
                        RefreshTable.refreshPaymentsTable(paymentsTable);
                    });
                }
            }
        });
        paymentsTable.getColumns().add(actionsColumn);
    }
}