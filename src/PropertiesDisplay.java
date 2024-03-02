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

public class PropertiesDisplay {
    private Stage stage;

    public PropertiesDisplay(Stage stage) {
        this.stage = stage;
    }

    public void showProperties() {
        // Create a TableView to display the properties
        TableView<Property> propertiesTable = new TableView<>();

        // Define the columns
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

        // Add the columns to the TableView
        propertiesTable.getColumns().addAll(idColumn, typeColumn, sizeColumn, locationColumn, priceColumn, furnishingStatusColumn);


        // Retrieve properties from the database
        ObservableList<Property> properties = FXCollections.observableArrayList();
        try (Connection con = DBUtils.establishConnection();
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT PropertyID, Type, Size, Location, Price, FurnishingStatus FROM Property");
            while (rs.next()) {
                Property property = new Property(rs.getInt("PropertyID"), rs.getString("Type"), rs.getString("Size"), rs.getString("Location"), rs.getString("Price"), rs.getString("FurnishingStatus"));
                properties.add(property);
            }
        } catch (Exception e) {
            // Handle exceptions (e.g., SQL errors)
            e.printStackTrace();
        }

        // Set the items for the TableView
        propertiesTable.setItems(properties);

        // Create a back button to return to the previous screen
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            // Go back to the DisplayOptions screen
            DisplayOptions displayOptions = new DisplayOptions(stage);
            displayOptions.showOptions();
        });

        // Create the layout for the properties display
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(propertiesTable, backButton);

        // Create the scene and set it on the stage
        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    // Property class to hold the data for each property
    public static class Property {
        private final int id;
        private final String type;
        private final String size;
        private final String location;
        private final String price;
        private final String furnishingStatus;

        public Property(int id, String type, String size, String location, String price, String furnishingStatus) {
            this.id = id;
            this.type = type;
            this.size = size;
            this.location = location;
            this.price = price;
            this.furnishingStatus = furnishingStatus;
        }

        public int getId() { return id; }
        public String getType() { return type; }
        public String getSize() { return size; }
        public String getLocation() { return location; }
        public String getPrice() { return price; }
        public String getFurnishingStatus() { return furnishingStatus; }
    }
}