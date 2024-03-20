package Menus.Maintenances;

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

public class ShowMaintenances {
    private final Stage stage;
    private final String userRole;

    public ShowMaintenances(Stage stage, String userRole) {
        this.stage = stage;
        this.userRole = userRole;
    }

    public void display() {
        TableView<Maintenance> maintenanceTable = new TableView<>();
        maintenanceTable.setEditable(true);

        // Define the columns for the maintenance table
        TableColumn<Maintenance, Integer> idColumn = new TableColumn<>("Request ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Maintenance, Integer> propertyIdColumn = new TableColumn<>("Property ID");
        propertyIdColumn.setCellValueFactory(new PropertyValueFactory<>("propertyId"));
        TableColumn<Maintenance, Integer> tenantIdColumn = new TableColumn<>("Tenant ID");
        tenantIdColumn.setCellValueFactory(new PropertyValueFactory<>("tenantId"));
        TableColumn<Maintenance, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<Maintenance, String> reportDateColumn = new TableColumn<>("Report Date");
        reportDateColumn.setCellValueFactory(new PropertyValueFactory<>("reportDate"));
        TableColumn<Maintenance, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        TableColumn<Maintenance, String> priorityColumn = new TableColumn<>("Priority");
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        TableColumn<Maintenance, String> resolutionDateColumn = new TableColumn<>("Resolution Date");
        resolutionDateColumn.setCellValueFactory(new PropertyValueFactory<>("resolutionDate"));

        maintenanceTable.getColumns().add(idColumn);
        maintenanceTable.getColumns().add(propertyIdColumn);
        maintenanceTable.getColumns().add(tenantIdColumn);
        maintenanceTable.getColumns().add(descriptionColumn);
        maintenanceTable.getColumns().add(reportDateColumn);
        maintenanceTable.getColumns().add(statusColumn);
        maintenanceTable.getColumns().add(priorityColumn);
        maintenanceTable.getColumns().add(resolutionDateColumn);

        ObservableList<Maintenance> maintenances = FXCollections.observableArrayList();
        try {
            List<Maintenance> allMaintenanceRequests = MaintenanceDAO.getAllMaintenanceRequests();
            maintenances.addAll(allMaintenanceRequests);
        } catch (SQLException e) {
            ShowAlert.display("Database Error", "Failed to load maintenance requests: " + e.getMessage(),
                    Alert.AlertType.WARNING);
        }

        maintenanceTable.setItems(maintenances);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            DisplayOptions displayOptions = new DisplayOptions(stage, this.userRole);
            displayOptions.showOptions();
        });

        Button addButton = new Button("Add New Maintenance Request");
        addButton.setOnAction(e -> new ShowAddMaintenanceForm(stage, userRole).display());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(maintenanceTable, addButton, backButton);

        Scene scene = new Scene(layout, 900, 400);
        stage.setScene(scene);
        stage.show();

        TableColumn<Maintenance, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(column -> new TableCell<Maintenance, Void>() {
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

                    Maintenance maintenance = getTableView().getItems().get(getIndex());

                    editButton.setOnAction(event -> {
                        new ShowEditMaintenanceForm(stage, maintenance, userRole).display();
                    });

                    removeButton.setOnAction(event -> {
                        try {
                            MaintenanceDAO.removeMaintenanceRequest(maintenance.getId());
                            maintenances.remove(maintenance);
                            ShowAlert.display("Maintenance Request Removed",
                                    "The maintenance request has been successfully removed.",
                                    Alert.AlertType.INFORMATION);
                        } catch (SQLException ex) {
                            ShowAlert.display("Database Error",
                                    "Failed to remove maintenance request: " + ex.getMessage(),
                                    Alert.AlertType.WARNING);
                        }
                    });
                }
            }
        });
        maintenanceTable.getColumns().add(actionsColumn);
    }
}