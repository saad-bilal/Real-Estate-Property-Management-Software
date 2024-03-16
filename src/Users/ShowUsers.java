package Users;

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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ShowUsers {
    private final Stage stage;
    private final String userRole;

    public ShowUsers(Stage stage, String userRole) {
        this.stage = stage;
        this.userRole = userRole;
    }

    public void display() {
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

        usersTable.getColumns().add(idColumn);
        usersTable.getColumns().add(firstNameColumn);
        usersTable.getColumns().add(lastNameColumn);
        usersTable.getColumns().add(usernameColumn);
        usersTable.getColumns().add(emailColumn);
        usersTable.getColumns().add(phoneNumberColumn);
        usersTable.getColumns().add(roleColumn);

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
            ShowAlert.display("Database Error", "Failed to load users: " + e.getMessage(), Alert.AlertType.WARNING);
        }

        usersTable.setItems(users);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            DisplayOptions displayOptions = new DisplayOptions(stage, this.userRole);
            displayOptions.showOptions();
        });

        Button addButton = new Button("Add New User");
        addButton.setOnAction(e -> new ShowAddUserForm(stage, userRole).display());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(usersTable, addButton, backButton);

        Scene scene = new Scene(layout, 900, 400);
        stage.setScene(scene);
        stage.show();

        TableColumn<User, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(column -> new TableCell<User, Void>() {
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

                    User user = getTableView().getItems().get(getIndex());

                    editButton.setOnAction(event -> {
                        new ShowEditUserForm(stage, user, userRole).display();
                    });

                    removeButton.setOnAction(event -> {
                        RemoveUser.removeUser(user.getId());
                        RefreshTable.refreshUsersTable(usersTable);
                    });
                }
            }
        });
        usersTable.getColumns().add(actionsColumn);
    }
}