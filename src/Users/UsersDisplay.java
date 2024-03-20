package Users;

import javafx.stage.Stage;

public class UsersDisplay {
    private final Stage stage;
    private final String userRole;

    public UsersDisplay(Stage stage, String userRole) {
        this.stage = stage;
        this.userRole = userRole;
    }

    public void showUsers() {
        new ShowUsers(stage, userRole).display();
    }

    public static void main(String[] args) {
    }
}