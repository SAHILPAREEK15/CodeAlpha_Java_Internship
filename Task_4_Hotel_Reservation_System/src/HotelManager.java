import java.io.*;
import java.util.*;

public class HotelManager {
    private static final String DATA_DIR = "data";
    private static final String BOOKINGS_FILE = DATA_DIR + "/bookings.txt";
    
    private List<Room> rooms;
    private List<Booking> bookings;
    
    public HotelManager() {
        this.rooms = new ArrayList<>();
        this.bookings = new ArrayList<>();
        createDataDirectoryIfNeeded();
        initializeRooms();
        loadBookings();
    }
    
    private void createDataDirectoryIfNeeded() {
        new File(DATA_DIR).mkdirs();
    }
    
    private void initializeRooms() {
        // Create standard rooms
        for (int i = 101; i <= 110; i++) {
            rooms.add(new Room(String.valueOf(i), Room.RoomCategory.STANDARD, 2));
        }
        
        // Create deluxe rooms
        for (int i = 201; i <= 210; i++) {
            rooms.add(new Room(String.valueOf(i), Room.RoomCategory.DELUXE, 3));
        }
        
        // Create suite rooms
        for (int i = 301; i <= 305; i++) {
            rooms.add(new Room(String.valueOf(i), Room.RoomCategory.SUITE, 4));
        }
        
        // Create premium rooms
        for (int i = 401; i <= 404; i++) {
            rooms.add(new Room(String.valueOf(i), Room.RoomCategory.PREMIUM, 4));
        }
    }
    
    public List<Room> getAvailableRooms(Date checkIn, Date checkOut) {
        List<Room> available = new ArrayList<>();
        
        for (Room room : rooms) {
            if (isRoomAvailable(room.getRoomNumber(), checkIn, checkOut)) {
                available.add(room);
            }
        }
        
        return available;
    }
    
    public List<Room> getAvailableRoomsByCategory(Room.RoomCategory category, Date checkIn, Date checkOut) {
        List<Room> available = new ArrayList<>();
        
        for (Room room : rooms) {
            if (room.getCategory() == category && isRoomAvailable(room.getRoomNumber(), checkIn, checkOut)) {
                available.add(room);
            }
        }
        
        return available;
    }
    
    private boolean isRoomAvailable(String roomNumber, Date checkIn, Date checkOut) {
        for (Booking booking : bookings) {
            if (booking.getRoomNumber().equals(roomNumber) && booking.getStatus().equals("Confirmed")) {
                // Check if dates overlap
                if (!checkOut.before(booking.getCheckInDate()) && !checkIn.after(booking.getCheckOutDate())) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public Room getRoom(String roomNumber) {
        return rooms.stream()
                .filter(r -> r.getRoomNumber().equals(roomNumber))
                .findFirst()
                .orElse(null);
    }
    
    public Booking createBooking(String guestName, String email, String phone, 
                                Date checkIn, Date checkOut, int guests, String roomNumber) {
        Room room = getRoom(roomNumber);
        if (room == null || !isRoomAvailable(roomNumber, checkIn, checkOut)) {
            return null;
        }
        
        Booking booking = new Booking(guestName, email, phone, checkIn, checkOut, guests, room);
        bookings.add(booking);
        saveBookings();
        
        return booking;
    }
    
    public Booking getBooking(String bookingId) {
        return bookings.stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElse(null);
    }
    
    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings);
    }
    
    public List<Booking> getBookingsByEmail(String email) {
        List<Booking> result = new ArrayList<>();
        for (Booking booking : bookings) {
            if (booking.getEmail().equals(email)) {
                result.add(booking);
            }
        }
        return result;
    }
    
    public void cancelBooking(String bookingId) {
        for (Booking booking : bookings) {
            if (booking.getBookingId().equals(bookingId)) {
                booking.setStatus("Cancelled");
                break;
            }
        }
        saveBookings();
    }
    
    public int getTotalRooms() {
        return rooms.size();
    }
    
    public int getAvailableRoomCount(Date checkIn, Date checkOut) {
        return getAvailableRooms(checkIn, checkOut).size();
    }
    
    public int getConfirmedBookingCount() {
        return (int) bookings.stream()
                .filter(b -> b.getStatus().equals("Confirmed"))
                .count();
    }
    
    public double getTotalRevenue() {
        return bookings.stream()
                .filter(b -> b.getStatus().equals("Confirmed"))
                .mapToDouble(Booking::getTotalPrice)
                .sum();
    }
    
    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms);
    }
    
    private void saveBookings() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BOOKINGS_FILE))) {
            oos.writeObject(bookings);
        } catch (IOException e) {
            System.err.println("Error saving bookings: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadBookings() {
        File file = new File(BOOKINGS_FILE);
        if (!file.exists()) {
            bookings = new ArrayList<>();
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            bookings = (List<Booking>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading bookings: " + e.getMessage());
            bookings = new ArrayList<>();
        }
    }
}
