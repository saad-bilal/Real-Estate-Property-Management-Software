package Menus.Properties;

import Utilities.ShowAlert;
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

public class ShowProperties {
    private final Stage stage;
    private final String userRole;

    public ShowProperties(Stage stage, String userRole) {
        this.stage = stage;
        this.userRole = userRole;
    }

    public void display() {
        TableView<Property> propertiesTable = new TableView<>();
        propertiesTable.setEditable(true);

        // Define the columns for the properties table
        TableColumn<Property, Integer> idColumn = new TableColumn<>("Property ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Property, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<Property, String> sizeColumn = new TableColumn<>("Size");
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        TableColumn<Property, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        TableColumn<Property, String> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<Property, String> furnishingStatusColumn = new TableColumn<>("Furnishing Status");
        furnishingStatusColumn.setCellValueFactory(new PropertyValueFactory<>("furnishingStatus"));
        TableColumn<Property, String> maintenanceHistoryColumn = new TableColumn<>("Maintenance History");
        maintenanceHistoryColumn.setCellValueFactory(new PropertyValueFactory<>("maintenanceHistory"));

        propertiesTable.getColumns().add(idColumn);
        propertiesTable.getColumns().add(typeColumn);
        propertiesTable.getColumns().add(sizeColumn);
        propertiesTable.getColumns().add(locationColumn);
        propertiesTable.getColumns().add(priceColumn);
        propertiesTable.getColumns().add(furnishingStatusColumn);
        propertiesTable.getColumns().add(maintenanceHistoryColumn);

        ObservableList<Property> properties = FXCollections.observableArrayList();

        try {
            List<Property> allProperties = PropertyDAO.getAllProperties();
            properties.addAll(allProperties);
            propertiesTable.setItems(properties);
        } catch (SQLException e) {
            ShowAlert.display("Database Error", "Failed to load properties: " + e.getMessage(),
                    Alert.AlertType.WARNING);
        }

        propertiesTable.setItems(properties);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            DisplayOptions displayOptions = new DisplayOptions(stage, this.userRole);
            displayOptions.showOptions();
        });

        Button addButton = new Button("Add New Property");
        addButton.setOnAction(e -> new ShowAddPropertyForm(stage, userRole).display());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(propertiesTable, addButton, backButton);

        Scene scene = new Scene(layout, 900, 400);
        stage.setScene(scene);
        stage.show();

        TableColumn<Property, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(column -> new TableCell<Property, Void>() {
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

                    Property property = getTableView().getItems().get(getIndex());

                    editButton.setOnAction(event -> {
                        new ShowEditPropertyForm(stage, property, userRole).display();
                    });

                    removeButton.setOnAction(event -> {
                        RemoveProperty.removeProperty(property.getId());
                        new ShowProperties(stage, userRole).display();
                    });
                }
            }
        });
        propertiesTable.getColumns().add(actionsColumn);
    }
}