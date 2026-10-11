import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String bookingId;
    private String guestName;
    private String email;
    private String phone;
    private Date checkInDate;
    private Date checkOutDate;
    private int numberOfGuests;
    private String roomNumber;
    private Room.RoomCategory roomCategory;
    private double pricePerNight;
    private int numberOfNights;
    private double totalPrice;
    private String paymentMethod; // "Cash", "Card", "UPI"
    private String status; // "Confirmed", "Cancelled"
    private long bookingTime;
    
    public Booking(String guestName, String email, String phone, Date checkInDate, Date checkOutDate,
                   int numberOfGuests, Room room) {
        this.bookingId = "BK" + System.currentTimeMillis();
        this.guestName = guestName;
        this.email = email;
        this.phone = phone;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfGuests = numberOfGuests;
        this.roomNumber = room.getRoomNumber();
        this.roomCategory = room.getCategory();
        this.pricePerNight = room.getPricePerNight();
        this.numberOfNights = calculateNights(checkInDate, checkOutDate);
        this.totalPrice = numberOfNights * pricePerNight;
        this.status = "Confirmed";
        this.bookingTime = System.currentTimeMillis();
    }
    
    private int calculateNights(Date checkIn, Date checkOut) {
        long diffTime = checkOut.getTime() - checkIn.getTime();
        long diffDays = diffTime / (1000 * 60 * 60 * 24);
        return Math.max(1, (int) diffDays);
    }
    
    // Getters
    public String getBookingId() { return bookingId; }
    public String getGuestName() { return guestName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public Date getCheckInDate() { return checkInDate; }
    public Date getCheckOutDate() { return checkOutDate; }
    public int getNumberOfGuests() { return numberOfGuests; }
    public String getRoomNumber() { return roomNumber; }
    public Room.RoomCategory getRoomCategory() { return roomCategory; }
    public double getPricePerNight() { return pricePerNight; }
    public int getNumberOfNights() { return numberOfNights; }
    public double getTotalPrice() { return totalPrice; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String method) { this.paymentMethod = method; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getCheckInString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(checkInDate);
    }
    
    public String getCheckOutString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(checkOutDate);
    }
    
    @Override
    public String toString() {
        return "Booking ID: " + bookingId + ", Guest: " + guestName + ", Room: " + roomNumber + 
               ", Status: " + status;
    }
}
