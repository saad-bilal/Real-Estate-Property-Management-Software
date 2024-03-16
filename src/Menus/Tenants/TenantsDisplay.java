package Menus.Tenants;

import javafx.stage.Stage;

public class TenantsDisplay {
    private final Stage stage;
    private final String userRole;

    public TenantsDisplay(Stage stage, String userRole) {
        this.stage = stage;
        this.userRole = userRole;
    }

    public void showTenants() {
        new ShowTenants(stage, userRole).display();
    }

    public static void main(String[] args) {    }
}