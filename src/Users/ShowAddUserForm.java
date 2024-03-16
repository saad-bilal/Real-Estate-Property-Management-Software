package Users;

import Utilities.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ShowAddUserForm {
    private final Stage stage;
    private final String userRole;

    public ShowAddUserForm(Stage stage, String userRole) {
        this.stage = stage;
        this.userRole = userRole;
    }

    public void display() {
        Stage addUserStage = new Stage();
        addUserStage.setTitle("Add New User");

        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        TextField emailField = new TextField();
        TextField phoneNumberField = new TextField();

        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Employee", "Admin");
        roleComboBox.getSelectionModel().selectFirst();

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            try (Connection con = DBUtils.establishConnection();
                 PreparedStatement pstmt = con.prepareStatement(
                        "INSERT INTO Users (FirstName, LastName, Username, Password, Email, PhoneNumber, Role) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

                pstmt.setString(1, firstNameField.getText());
                pstmt.setString(2, lastNameField.getText());
                pstmt.setString(3, usernameField.getText());
                pstmt.setString(4, passwordField.getText());
                pstmt.setString(5, emailField.getText());
                pstmt.setString(6, phoneNumberField.getText());
                pstmt.setString(7, roleComboBox.getSelectionModel().getSelectedItem());
                pstmt.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("User Added");
                alert.setContentText("New user added successfully.");
                alert.showAndWait();

                new ShowUsers(stage, this.userRole).display();
                addUserStage.close();
            } catch (SQLException ex) {
                ShowAlert.display("Database Error", "Failed to add new user.", Alert.AlertType.WARNING);
            }
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(
                new Label("First Name:"), firstNameField,
                new Label("Last Name:"), lastNameField,
                new Label("Username:"), usernameField,
                new Label("Password:"), passwordField,
                new Label("Email:"), emailField,
                new Label("Phone Number:"), phoneNumberField,
                new Label("Role:"), roleComboBox,
                submitButton);

        Scene scene = new Scene(layout, 300, 500);
        addUserStage.setScene(scene);
        addUserStage.show();
    }
}