package Menus.Properties;

public class Property {
    private final int id;
    private final String type;
    private final String size;
    private final String location;
    private final String price;
    private final String furnishingStatus;
    private final String maintenanceHistory;

    public Property(int id, String type, String size, String location, String price, String furnishingStatus, String maintenanceHistory) {
        this.id = id;
        this.type = type;
        this.size = size;
        this.location = location;
        this.price = price;
        this.furnishingStatus = furnishingStatus;
        this.maintenanceHistory = maintenanceHistory;

    }

    public int getId() { return id; }
    public String getType() { return type; }
    public String getSize() { return size; }
    public String getLocation() { return location; }
    public String getPrice() { return price; }
    public String getFurnishingStatus() { return furnishingStatus; }
    public String getMaintenanceHistory() { return maintenanceHistory; }

}