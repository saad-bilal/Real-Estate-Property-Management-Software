package Menus.Maintenances;

import javafx.stage.Stage;

public class MaintenancesDisplay {
    private final Stage stage;
    private final String userRole;

    public MaintenancesDisplay(Stage stage, String userRole) {
        this.stage = stage;
        this.userRole = userRole;
    }

    public void showMaintenances() {
        new ShowMaintenances(stage, userRole).display();
    }

    public static void main(String[] args) {
    }
}