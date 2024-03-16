package Menus.Payments;

public class Payment {
    private final int id;
    private final int leaseId;
    private final int tenantId;
    private final String paymentDate;
    private final String amount;
    private final String paymentType;
    private final String description;
    private final String receiptNumber;

    public Payment(int id, int leaseId, int tenantId, String paymentDate, String amount, String paymentType, String description, String receiptNumber) {
        this.id = id;
        this.leaseId = leaseId;
        this.tenantId = tenantId;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.paymentType = paymentType;
        this.description = description;
        this.receiptNumber = receiptNumber;
    }

    // Getter methods for each field
    public int getId() { return id; }
    public int getLeaseId() { return leaseId; }
    public int getTenantId() { return tenantId; }
    public String getPaymentDate() { return paymentDate; }
    public String getAmount() { return amount; }
    public String getPaymentType() { return paymentType; }
    public String getDescription() { return description; }
    public String getReceiptNumber() { return receiptNumber; }
}