package Menus.Leases;

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

public class ShowLeases {
    private final Stage stage;
    private final String userRole;

    public ShowLeases(Stage stage, String userRole) {
        this.stage = stage;
        this.userRole = userRole;
    }

    public void display() {
        TableView<Lease> leasesTable = new TableView<>();
        leasesTable.setEditable(true);

        // Define the columns for the leases table
        TableColumn<Lease, Integer> idColumn = new TableColumn<>("Lease ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Lease, Integer> propertyIdColumn = new TableColumn<>("Property ID");
        propertyIdColumn.setCellValueFactory(new PropertyValueFactory<>("propertyId"));
        TableColumn<Lease, Integer> tenantIdColumn = new TableColumn<>("Tenant ID");
        tenantIdColumn.setCellValueFactory(new PropertyValueFactory<>("tenantId"));
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

        leasesTable.getColumns().add(idColumn);
        leasesTable.getColumns().add(propertyIdColumn);
        leasesTable.getColumns().add(tenantIdColumn);
        leasesTable.getColumns().add(startDateColumn);
        leasesTable.getColumns().add(endDateColumn);
        leasesTable.getColumns().add(monthlyRentColumn);
        leasesTable.getColumns().add(securityDepositColumn);
        leasesTable.getColumns().add(signatureDateColumn);
        leasesTable.getColumns().add(statusColumn);

        ObservableList<Lease> leases = FXCollections.observableArrayList();
        try (Connection con = DBUtils.establishConnection();
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt
                    .executeQuery("SELECT LeaseID, PropertyID, TenantID, StartDate, EndDate, MonthlyRent, SecurityDeposit, SignatureDate, Status FROM LeaseAgreements");
                    while (rs.next()) {
                        leases.add(new Lease(rs.getInt("LeaseID"), rs.getInt("PropertyID"), rs.getInt("TenantID"),
                                rs.getDate("StartDate").toString(), rs.getDate("EndDate").toString(),
                                rs.getString("MonthlyRent"), rs.getString("SecurityDeposit"),
                                rs.getDate("SignatureDate").toString(), rs.getString("Status")));
                    }
        } catch (Exception e) {
            ShowAlert.display("Database Error", "Failed to load leases: " + e.getMessage(),
                    Alert.AlertType.WARNING);
        }

        leasesTable.setItems(leases);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            DisplayOptions displayOptions = new DisplayOptions(stage, this.userRole);
            displayOptions.showOptions();
        });

        Button addButton = new Button("Add New Lease");
        addButton.setOnAction(e -> new ShowAddLeaseForm(stage, userRole).display());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(leasesTable, addButton, backButton);

        Scene scene = new Scene(layout, 900, 400);
        stage.setScene(scene);
        stage.show();

        TableColumn<Lease, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(column -> new TableCell<Lease, Void>() {
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

                    Lease lease = getTableView().getItems().get(getIndex());

                    editButton.setOnAction(event -> {
                        new ShowEditLeaseForm(stage, lease, userRole).display();
                    });

                    removeButton.setOnAction(event -> {
                        RemoveLease.removeLease(lease.getId());
                        RefreshTable.refreshLeasesTable(leasesTable);
                    });
                }
            }
        });
        leasesTable.getColumns().add(actionsColumn);
    }
}