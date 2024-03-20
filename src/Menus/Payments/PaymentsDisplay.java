package Menus.Payments;

import javafx.stage.Stage;

public class PaymentsDisplay {
    private final Stage stage;
    private final String userRole;

    public PaymentsDisplay(Stage stage, String userRole) {
        this.stage = stage;
        this.userRole = userRole;
    }

    public void showPayments() {
        new ShowPayments(stage, userRole).display();
    }

    public static void main(String[] args) {
    }
}