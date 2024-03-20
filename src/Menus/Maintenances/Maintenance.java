package Menus.Maintenances;

public class Maintenance {
    private final int id;
    private final int propertyId;
    private final int tenantId;
    private final String description;
    private final String reportDate;
    private final String status;
    private final String priority;
    private final String resolutionDate;

    public Maintenance(int id, int propertyId, int tenantId, String description, String reportDate, String status,
            String priority, String resolutionDate) {
        this.id = id;
        this.propertyId = propertyId;
        this.tenantId = tenantId;
        this.description = description;
        this.reportDate = reportDate;
        this.status = status;
        this.priority = priority;
        this.resolutionDate = resolutionDate;
    }

    public int getId() {
        return id;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public int getTenantId() {
        return tenantId;
    }

    public String getDescription() {
        return description;
    }

    public String getReportDate() {
        return reportDate;
    }

    public String getStatus() {
        return status;
    }

    public String getPriority() {
        return priority;
    }

    public String getResolutionDate() {
        return resolutionDate;
    }
}