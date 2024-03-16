package Menus.Leases;

import javafx.stage.Stage;

public class LeasesDisplay {
    private final Stage stage;
    private final String userRole;

    public LeasesDisplay(Stage stage, String userRole) {
        this.stage = stage;
        this.userRole = userRole;
    }

    public void showLeases() {
        new ShowLeases(stage, userRole).display();
    }

    public static void main(String[] args) {
        // Example usage
        Stage stage = new Stage();
        String userRole = "admin"; // Example user role
        LeasesDisplay leasesDisplay = new LeasesDisplay(stage, userRole);
        leasesDisplay.showLeases();
    }
}