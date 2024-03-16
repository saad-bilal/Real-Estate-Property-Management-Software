package Menus;
// UserLogin.java

import Utilities.DBUtils;
import MainMenu.DisplayOptions;
import java.sql.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Class to handle user login
public class UserLogin {
    // Stage on which the current scene will be shown
    private Stage stage;
    // Text field for the username
    private TextField usernameField;
    // Password field for the password
    private PasswordField passwordField;

    // Constructor that initializes the stage
    public UserLogin(Stage stage) {
        this.stage = stage;
    }

    // Method to initialize the components of the login screen
    public void initializeComponents() {
        // Create a VBox layout with a spacing of 10
        VBox layout = new VBox(10);
        // Set padding around the layout
        layout.setPadding(new Insets(20, 20, 20, 20));

        // Create a label and text field for the username
        Label usernameLabel = new Label("Username:");
        usernameField = new TextField();

        // Create a label and password field for the password
        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();

        // Create a login button
        Button loginButton = new Button("Login");
        // Set the action for the login button
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Attempt to login when the button is clicked
                login();
            }
        });

        // Add all components to the layout
        layout.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, loginButton);

        // Create a scene with the layout and set it on the stage
        Scene scene = new Scene(layout, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    // Method to handle the login process
    private void login() {
        // Get the username and password from the text fields
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Try to establish a connection to the database
        try (Connection con = DBUtils.establishConnection();
                // Prepare a SQL statement to check the username and password
                PreparedStatement pstmt = con
                        .prepareStatement("SELECT * FROM Users WHERE Username = ? AND Password = ?")) {
            // Set the username and password in the SQL statement
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            // Execute the SQL query
            ResultSet rs = pstmt.executeQuery();

            // If a row is returned, the username and password are correct
            if (rs.next()) {
                String userRole = rs.getString("Role"); // Assuming the role column is named 'Role'
                DisplayOptions displayOptions = new DisplayOptions(stage, userRole);
                displayOptions.showOptions();
            } else {
                // Authentication failed, show an alert
                showAlert("Login Failed", "Invalid username or password.");
            }
        } catch (SQLException e) {
            // Handle exceptions (e.g., SQL errors)
            e.printStackTrace();
            showAlert("Database Error", "Failed to connect to the database.");
        }
    }

    // Method to show an alert dialog
    private void showAlert(String title, String content) {
        // Create an alert with the given title and content
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        // Show the alert and wait for the user to close it
        alert.showAndWait();
    }
}
