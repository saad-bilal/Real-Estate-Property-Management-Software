
// UserLogin.java
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

public class UserLogin {
    private Stage stage;
    private TextField usernameField;
    private PasswordField passwordField;

    public UserLogin(Stage stage) {
        this.stage = stage;
    }

    public void initializeComponents() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));

        Label usernameLabel = new Label("Username:");
        usernameField = new TextField();

        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();

        Button loginButton = new Button("Login");
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                login();
            }
        });

        layout.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, loginButton);

        Scene scene = new Scene(layout, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    private void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try (Connection con = DBUtils.establishConnection();
                PreparedStatement pstmt = con
                        .prepareStatement("SELECT * FROM Users WHERE Username = ? AND Password = ?")) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // User is authenticated, proceed to the next screen
                DisplayOptions displayOptions = new DisplayOptions(stage);
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

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}