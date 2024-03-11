import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button; // Import the Button class
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView; // Import the TableView class
import javafx.scene.control.TableCell; // Import the TableCell class
import javafx.scene.control.TableColumn;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.PreparedStatement; // Import the PreparedStatement class
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import java.sql.SQLException;

import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;

public class UsersDisplay {
    private Stage stage;
    private String userRole; // User's role for conditional display

    public UsersDisplay(Stage stage, String userRole) {
        this.stage = stage;
        this.userRole = userRole; // Use this to control the display based on the user's role
    }

    public void showAddUserForm() {
        Stage addUserStage = new Stage();
        addUserStage.setTitle("Add New User");

        // Create form fields
        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        TextField emailField = new TextField();
        TextField phoneNumberField = new TextField();
        // TextField roleField = new TextField();

        // Create a ComboBox for the role selection
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Employee", "Admin");
        SingleSelectionModel<String> selectionModel = roleComboBox.getSelectionModel();
        selectionModel.selectFirst(); // Set the default selection to "Employee"

        // Create submit button
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            // Get user input
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();
            String email = emailField.getText();
            String phoneNumber = phoneNumberField.getText();
            // String role = roleField.getText();
            String role = roleComboBox.getSelectionModel().getSelectedItem();

            // Insert new user into the database
            try (Connection con = DBUtils.establishConnection();
                    PreparedStatement pstmt = con.prepareStatement(
                            "INSERT INTO Users (FirstName, LastName, Username, Password, Email, PhoneNumber, Role) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                pstmt.setString(1, firstName);
                pstmt.setString(2, lastName);
                pstmt.setString(3, username);
                pstmt.setString(4, password); // Make sure to hash the password before storing it in the database
                pstmt.setString(5, email);
                pstmt.setString(6, phoneNumber);
                pstmt.setString(7, role);

                // Execute the insert statement
                pstmt.executeUpdate();

                // Show a success message and close the add user form
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("User Added");
                alert.setHeaderText(null);
                alert.setContentText("New user added successfully.");
                alert.showAndWait();

                // Refresh the users table
                showUsers();

                // Close the add user form
                addUserStage.close();
            } catch (SQLException ex) {
                // Handle exceptions (e.g., SQL errors)
                ex.printStackTrace();
                showAlert("Database Error", "Failed to add new user.");
            }
        });

        // Create the layout for the add user form
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(
                new Label("First Name:"), firstNameField,
                new Label("Last Name:"), lastNameField,
                new Label("Username:"), usernameField,
                new Label("Password:"), passwordField,
                new Label("Email:"), emailField,
                new Label("Phone Number:"), phoneNumberField,
                // new Label("Role:"), roleField,
                new Label("Role:"), roleComboBox,
                submitButton);

        // Create the scene and set it on the stage
        Scene scene = new Scene(layout, 300, 200);
        addUserStage.setScene(scene);
        addUserStage.show();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void showUsers() {
        TableView<User> usersTable = new TableView<>();
        usersTable.setEditable(true);

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

        TableColumn<User, Void> actionsColumn = new TableColumn<>("Actions");
        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                return new TableCell<>() {
                    private final Button editButton = new Button("Edit");
                    private final Button removeButton = new Button("Remove");
                    {
                        editButton.setOnAction(event -> {
                            User user = getTableView().getItems().get(getIndex());
                            showEditUserForm(user); // Call the new method to show the edit user form
                            System.out.println("Editing user: " + user.getUsername());
                        });
                        removeButton.setOnAction(event -> {
                            User user = getTableView().getItems().get(getIndex());
                            // Show a confirmation dialog
                            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                            confirmation.setTitle("Confirm User Removal");
                            confirmation.setHeaderText(null);
                            confirmation.setContentText("Are you sure you want to remove this user?");

                            Optional<ButtonType> result = confirmation.showAndWait();
                            if (result.isPresent() && result.get() == ButtonType.OK) {
                                // If the admin confirms, remove the user from the database
                                try (Connection con = DBUtils.establishConnection();
                                        PreparedStatement pstmt = con
                                                .prepareStatement("DELETE FROM Users WHERE UserID = ?")) {
                                    pstmt.setInt(1, user.getId());
                                    pstmt.executeUpdate();

                                    // Refresh the users table
                                    showUsers();
                                } catch (SQLException ex) {
                                    // Handle exceptions (e.g., SQL errors)
                                    ex.printStackTrace();
                                    showAlert("Database Error", "Failed to remove the user.");
                                }
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : new HBox(editButton, removeButton));
                    }
                };
            }
        };
        actionsColumn.setCellFactory(cellFactory);

        usersTable.getColumns().add(idColumn);
        usersTable.getColumns().add(firstNameColumn);
        usersTable.getColumns().add(lastNameColumn);
        usersTable.getColumns().add(usernameColumn);
        usersTable.getColumns().add(emailColumn);
        usersTable.getColumns().add(phoneNumberColumn);
        usersTable.getColumns().add(roleColumn);
        usersTable.getColumns().add(actionsColumn);

        ObservableList<User> users = FXCollections.observableArrayList();
        try (Connection con = DBUtils.establishConnection();
                Statement stmt = con.createStatement()) {
            ResultSet rs = stmt
                    .executeQuery("SELECT UserID, FirstName, LastName, Username, Email, PhoneNumber, Role FROM Users");
            while (rs.next()) {
                users.add(new User(rs.getInt("UserID"), rs.getString("FirstName"), rs.getString("LastName"),
                        rs.getString("Username"), rs.getString("Email"), rs.getString("PhoneNumber"),
                        rs.getString("Role")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        usersTable.setItems(users);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            DisplayOptions displayOptions = new DisplayOptions(stage, userRole);
            displayOptions.showOptions();
        });

        Button addButton = new Button("Add New User");
        addButton.setOnAction(e -> {
            showAddUserForm();
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(usersTable, addButton, backButton);

        Scene scene = new Scene(layout, 900, 400);
        stage.setScene(scene);
        stage.show();
    }

    public static class User {
        private final int id;
        private final String firstName;
        private final String lastName;
        private final String username;
        private final String email;
        private final String phoneNumber;
        private final String role;

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

    public void showEditUserForm(User user) {
        Stage editUserStage = new Stage();
        editUserStage.setTitle("Edit User");

        // Create form fields and populate them with the user's current details
        TextField firstNameField = new TextField(user.getFirstName());
        TextField lastNameField = new TextField(user.getLastName());
        TextField usernameField = new TextField(user.getUsername());
        TextField emailField = new TextField(user.getEmail());
        TextField phoneNumberField = new TextField(user.getPhoneNumber());
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Employee", "Admin");
        roleComboBox.getSelectionModel().select(user.getRole()); // Select the current role

        // Create submit button
        Button submitButton = new Button("Update");
        submitButton.setOnAction(e -> {
            // Get user input
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String username = usernameField.getText();
            String email = emailField.getText();
            String phoneNumber = phoneNumberField.getText();
            String role = roleComboBox.getSelectionModel().getSelectedItem();

            // Update the user in the database
            try (Connection con = DBUtils.establishConnection();
                    PreparedStatement pstmt = con.prepareStatement(
                            "UPDATE Users SET FirstName = ?, LastName = ?, Username = ?, Email = ?, PhoneNumber = ?, Role = ? WHERE UserID = ?")) {
                pstmt.setString(1, firstName);
                pstmt.setString(2, lastName);
                pstmt.setString(3, username);
                pstmt.setString(4, email);
                pstmt.setString(5, phoneNumber);
                pstmt.setString(6, role);
                pstmt.setInt(7, user.getId());

                // Execute the update statement
                pstmt.executeUpdate();

                // Show a success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("User Updated");
                alert.setHeaderText(null);
                alert.setContentText("User details updated successfully.");
                alert.showAndWait();

                // Refresh the users table
                showUsers();

                // Close the edit user form
                editUserStage.close();
            } catch (SQLException ex) {
                // Handle exceptions (e.g., SQL errors)
                ex.printStackTrace();
                showAlert("Database Error", "Failed to update user details.");
            }
        });

        // Create the layout for the edit user form
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(
                new Label("First Name:"), firstNameField,
                new Label("Last Name:"), lastNameField,
                new Label("Username:"), usernameField,
                new Label("Email:"), emailField,
                new Label("Phone Number:"), phoneNumberField,
                new Label("Role:"), roleComboBox,
                submitButton);

        // Create the scene and set it on the stage
        Scene scene = new Scene(layout, 300, 400); // Adjust size as needed
        editUserStage.setScene(scene);
        editUserStage.show();
    }

}
