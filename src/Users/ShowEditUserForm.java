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

public class ShowEditUserForm {
    private final Stage stage;
    private final User user;
    private final String userRole;

    public ShowEditUserForm(Stage stage, User user, String userRole) {
        this.stage = stage;
        this.user = user;
        this.userRole = userRole;
    }

    public void display() {
        Stage editUserStage = new Stage();
        editUserStage.setTitle("Edit User");

        TextField firstNameField = new TextField(user.getFirstName());
        TextField lastNameField = new TextField(user.getLastName());
        TextField usernameField = new TextField(user.getUsername());
        TextField emailField = new TextField(user.getEmail());
        TextField phoneNumberField = new TextField(user.getPhoneNumber());

        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Employee", "Admin");
        roleComboBox.getSelectionModel().select(user.getRole());

        Button submitButton = new Button("Update");
        submitButton.setOnAction(e -> {
            try (Connection con = DBUtils.establishConnection();
                 PreparedStatement pstmt = con.prepareStatement(
                        "UPDATE Users SET FirstName = ?, LastName = ?, Username = ?, Email = ?, PhoneNumber = ?, Role = ? WHERE UserID = ?")) {

                pstmt.setString(1, firstNameField.getText());
                pstmt.setString(2, lastNameField.getText());
                pstmt.setString(3, usernameField.getText());
                pstmt.setString(4, emailField.getText());
                pstmt.setString(5, phoneNumberField.getText());
                pstmt.setString(6, roleComboBox.getSelectionModel().getSelectedItem());
                pstmt.setInt(7, user.getId());
                pstmt.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("User Updated");
                alert.setContentText("User details updated successfully.");
                alert.showAndWait();

                new ShowUsers(stage, this.userRole).display();
                editUserStage.close();
            } catch (SQLException ex) {
                ShowAlert.display("Database Error", "Failed to update user details.", Alert.AlertType.WARNING);
            }
        });

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

        Scene scene = new Scene(layout, 300, 430);
        editUserStage.setScene(scene);
        editUserStage.show();
    }
}