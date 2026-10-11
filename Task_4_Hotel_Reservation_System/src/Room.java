import java.io.Serializable;

public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum RoomCategory {
        STANDARD("Standard", 3000),
        DELUXE("Deluxe", 4500),
        SUITE("Suite", 7500),
        PREMIUM("Premium", 9000);
        
        private final String displayName;
        private final double pricePerNight;
        
        RoomCategory(String displayName, double pricePerNight) {
            this.displayName = displayName;
            this.pricePerNight = pricePerNight;
        }
        
        public String getDisplayName() { return displayName; }
        public double getPricePerNight() { return pricePerNight; }
    }
    
    private String roomNumber;
    private RoomCategory category;
    private int capacity;
    private boolean available;
    private String[] facilities;
    
    public Room(String roomNumber, RoomCategory category, int capacity) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.capacity = capacity;
        this.available = true;
        this.facilities = new String[]{"Free WiFi", "AC", "Bathroom", "TV", "Room Service"};
    }
    
    public String getRoomNumber() { return roomNumber; }
    public RoomCategory getCategory() { return category; }
    public double getPricePerNight() { return category.getPricePerNight(); }
    public int getCapacity() { return capacity; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public String[] getFacilities() { return facilities; }
    
    @Override
    public String toString() {
        return "Room " + roomNumber + " - " + category.getDisplayName() + " (₹" + getPricePerNight() + "/night)";
    }
}
