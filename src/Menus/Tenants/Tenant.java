package Menus.Tenants;

public class Tenant {
    private final int id;
    private final String name;
    private final String email;
    private final String phoneNumber;
    private final String moveInDate;
    private final String moveOutDate;
    private final String paymentHistory;

    public Tenant(int id, String name, String email, String phoneNumber, String moveInDate, String moveOutDate, String paymentHistory) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.moveInDate = moveInDate;
        this.moveOutDate = moveOutDate;
        this.paymentHistory = paymentHistory;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getMoveInDate() { return moveInDate; }
    public String getMoveOutDate() { return moveOutDate; }
    public String getPaymentHistory() { return paymentHistory; }
}