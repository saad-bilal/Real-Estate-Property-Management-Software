package Menus.Tenants;

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
import java.sql.SQLException;
import java.util.List;

public class ShowTenants {
    private final Stage stage;
    private final String userRole;

    public ShowTenants(Stage stage, String userRole) {
        this.stage = stage;
        this.userRole = userRole;
    }

    public void display() {
        TableView<Tenant> tenantsTable = new TableView<>();
        tenantsTable.setEditable(true);

        // Define the columns for the tenants table
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
        TableColumn<Tenant, String> paymentHistoryColumn = new TableColumn<>("Payment History");
        paymentHistoryColumn.setCellValueFactory(new PropertyValueFactory<>("paymentHistory"));

        tenantsTable.getColumns().add(idColumn);
        tenantsTable.getColumns().add(nameColumn);
        tenantsTable.getColumns().add(emailColumn);
        tenantsTable.getColumns().add(phoneNumberColumn);
        tenantsTable.getColumns().add(moveInDateColumn);
        tenantsTable.getColumns().add(moveOutDateColumn);
        tenantsTable.getColumns().add(paymentHistoryColumn);

        ObservableList<Tenant> tenants = FXCollections.observableArrayList();
        try {
            List<Tenant> allTenants = TenantDAO.getAllTenants();
            tenants.addAll(allTenants);
        } catch (SQLException e) {
            ShowAlert.display("Database Error", "Failed to load tenants: " + e.getMessage(), Alert.AlertType.WARNING);
        }

        tenantsTable.setItems(tenants);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            DisplayOptions displayOptions = new DisplayOptions(stage, this.userRole);
            displayOptions.showOptions();
        });

        Button addButton = new Button("Add New Tenant");
        addButton.setOnAction(e -> new ShowAddTenantForm(stage, userRole).display());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(tenantsTable, addButton, backButton);

        Scene scene = new Scene(layout, 900, 400);
        stage.setScene(scene);
        stage.show();

        TableColumn<Tenant, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(column -> new TableCell<Tenant, Void>() {
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

                    Tenant tenant = getTableView().getItems().get(getIndex());

                    editButton.setOnAction(event -> {
                        new ShowEditTenantForm(stage, tenant, userRole).display();
                    });

                    removeButton.setOnAction(event -> {
                        try {
                            TenantDAO.removeTenant(tenant.getId());
                            tenants.remove(tenant);
                            ShowAlert.display("Tenant Removed", "The tenant has been successfully removed.",
                                    Alert.AlertType.INFORMATION);
                        } catch (SQLException ex) {
                            ShowAlert.display("Database Error", "Failed to remove tenant: " + ex.getMessage(),
                                    Alert.AlertType.WARNING);
                        }
                    });
                }
            }
        });
        tenantsTable.getColumns().add(actionsColumn);
    }
}