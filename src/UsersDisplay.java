
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

// Class to display users in a table
public class UsersDisplay {
    // Stage on which the current scene will be shown
    private Stage stage;

    // Constructor that initializes the stage
    public UsersDisplay(Stage stage) {
        this.stage = stage;
    }

    // Method to show the users in a table
    public void showUsers() {
        // Create a TableView to display the users
        TableView<User> usersTable = new TableView<>();

        // Define the columns for the table
        TableColumn<User, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<User, String> firstNameColumn = new TableColumn<>("First Name");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<User, String> lastNameColumn = new TableColumn<>("Last Name");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<User, String> phoneNumberColumn = new TableColumn<>("Phone Number");
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        TableColumn<User, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Add the columns to the TableView one by one
        usersTable.getColumns().add(idColumn);
        usersTable.getColumns().add(firstNameColumn);
        usersTable.getColumns().add(lastNameColumn);
        usersTable.getColumns().add(usernameColumn);
        usersTable.getColumns().add(emailColumn);
        usersTable.getColumns().add(phoneNumberColumn);
        usersTable.getColumns().add(roleColumn);

        // Retrieve users from the database
        ObservableList<User> users = FXCollections.observableArrayList();
        try (Connection con = DBUtils.establishConnection();
                Statement stmt = con.createStatement()) {
            ResultSet rs = stmt
                    .executeQuery("SELECT UserID, FirstName, LastName, Username, Email, PhoneNumber, Role FROM Users");
            while (rs.next()) {
                User user = new User(rs.getInt("UserID"), rs.getString("FirstName"), rs.getString("LastName"),
                        rs.getString("Username"), rs.getString("Email"), rs.getString("PhoneNumber"),
                        rs.getString("Role"));
                users.add(user);
            }
        } catch (Exception e) {
            // Handle exceptions (e.g., SQL errors)
            e.printStackTrace();
        }

        // Set the items for the TableView
        usersTable.setItems(users);

        // Create a back button to return to the previous screen
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            // Go back to the DisplayOptions screen
            DisplayOptions displayOptions = new DisplayOptions(stage);
            displayOptions.showOptions();
        });

        // Create the layout for the users display
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(usersTable, backButton);

        // Create the scene and set it on the stage
        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    // User class to hold the data for each user
    public static class User {
        private final int id;
        private final String firstName;
        private final String lastName;
        private final String username;
        private final String email;
        private final String phoneNumber;
        private final String role;

        // Constructor for the User class
        public User(int id, String firstName, String lastName, String username, String email, String phoneNumber,
                String role) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.username = username;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.role = role;
        }

        // Getters for the User class
        public int getId() {
            return id;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getRole() {
            return role;
        }
    }
}