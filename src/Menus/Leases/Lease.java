package Menus.Leases;

public class Lease {
    private final int id;
    private final int propertyId;
    private final int tenantId;
    private final String startDate;
    private final String endDate;
    private final String monthlyRent;
    private final String securityDeposit;
    private final String signatureDate;
    private final String status;

    public Lease(int id, int propertyId, int tenantId, String startDate, String endDate, String monthlyRent,
            String securityDeposit, String signatureDate, String status) {
        this.id = id;
        this.propertyId = propertyId;
        this.tenantId = tenantId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.monthlyRent = monthlyRent;
        this.securityDeposit = securityDeposit;
        this.signatureDate = signatureDate;
        this.status = status;
    }

    // Getter methods for each field
    public int getId() {
        return id;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public int getTenantId() {
        return tenantId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getMonthlyRent() {
        return monthlyRent;
    }

    public String getSecurityDeposit() {
        return securityDeposit;
    }

    public String getSignatureDate() {
        return signatureDate;
    }

    public String getStatus() {
        return status;
    }
}