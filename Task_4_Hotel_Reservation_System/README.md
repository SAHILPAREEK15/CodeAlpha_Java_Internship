# Task 4: Hotel Reservation System

## Project Overview
A complete hotel room booking and management system built in Java Swing with real-time availability checking, booking confirmation, and admin dashboard for hotel managers.

## Features
- **Home Page**: Hotel introduction with room categories and quick links
- **Search & Filter**:
  - Filter by check-in/check-out dates
  - Filter by guest count
  - Filter by room category
  - Real-time availability display
- **Room Categories**:
  - Standard: ₹3,000/night (Capacity: 2)
  - Deluxe: ₹4,500/night (Capacity: 3)
  - Suite: ₹7,500/night (Capacity: 4)
  - Premium: ₹9,000/night (Capacity: 4)
- **Booking System**:
  - Guest details collection (Name, Email, Phone)
  - Automatic price calculation
  - Payment method selection (Cash, Card, UPI)
  - Booking confirmation with ID
- **My Bookings**: View and manage personal bookings by email
- **Admin Dashboard**: 
  - Total rooms and occupancy statistics
  - Revenue tracking
  - All bookings overview
  - Booking status management

## Technologies Used
- Java 17+
- Java Swing for GUI
- Date/Calendar API for date handling
- Collections Framework
- Object serialization for data persistence

## OOP Concepts Used
- **Encapsulation**: Room, Booking classes with private fields
- **Inheritance**: Panel classes extending JPanel
- **Polymorphism**: Method overriding in panel refresh
- **Abstraction**: HotelManager hiding business logic
- **Enums**: RoomCategory enumeration
- **Collections**: ArrayList for rooms and bookings

## Room Management
- Total 39 rooms across 4 categories
- 10 Standard rooms (101-110)
- 10 Deluxe rooms (201-210)
- 5 Suite rooms (301-305)
- 4 Premium rooms (401-404)

## Project Structure
```
Task_4_Hotel_Reservation_System/
├── src/
│   ├── HotelReservationApp.java         (Main application)
│   ├── Room.java                        (Room model)
│   ├── Booking.java                     (Booking model)
│   ├── HotelManager.java                (Business logic)
│   ├── HomePanel.java                   (Home/Welcome page)
│   ├── SearchRoomsPanel.java            (Room search & filter)
│   ├── BookingDialog.java               (Booking form)
│   ├── MyBookingsPanel.java             (User bookings view)
│   └── AdminPanel.java                  (Admin dashboard)
├── data/
│   └── bookings.txt                     (Booking records)
└── README.md
```

## Compilation & Execution

### Compile:
```bash
cd Task_4_Hotel_Reservation_System/src
javac *.java
```

### Run:
```bash
java HotelReservationApp
```

## How to Use

### User Flow:
1. **Home**: Browse room categories and hotel features
2. **Search Rooms**:
   - Select check-in and check-out dates
   - Enter number of guests
   - Choose room category (or All)
   - Click "Search"
3. **Book Room**: Click "Book" on desired room
4. **Booking Form**:
   - Enter guest name, email, phone
   - Select payment method
   - Click "Confirm Booking"
5. **My Bookings**:
   - Enter email to retrieve bookings
   - View booking details
   - Cancel bookings if needed

### Admin Flow:
1. Navigate to "Admin" tab
2. View statistics: Total rooms, Available, Bookings, Revenue
3. View all bookings in table
4. Monitor occupancy and revenue

## Pricing Model
- Standard: ₹3,000/night
- Deluxe: ₹4,500/night
- Suite: ₹7,500/night
- Premium: ₹9,000/night

**Calculation**: Total = Number of Nights × Price per Night

## Booking Rules
- Check-out date must be after check-in date
- Minimum 1 night stay required
- Guest count must match room capacity
- Same room cannot be double-booked
- Cancellations possible anytime
- Payment methods: Cash, Card, UPI (simulated)

## Input Validation
- Email format validation
- Phone number validation
- All required fields must be filled
- Date validation (future dates only)
- Capacity check (guests ≤ room capacity)

## Data Storage
- Bookings stored in: `data/bookings.txt`
- Serialized Java objects
- Persistent across sessions
- Automatic file creation on first run

## Booking ID Format
- Format: `BK[timestamp]`
- Example: `BK1695847392001`
- Unique for each booking

## Booking Status
- **Confirmed**: Active booking
- **Cancelled**: User-cancelled booking

## Reports & Analytics
The admin panel provides:
- Total rooms in hotel
- Current available rooms
- Number of confirmed bookings
- Total revenue from bookings

## Limitations
- Single hotel location
- No user authentication
- Cash payment is simulated (not real)
- No email notification system
- Basic payment simulation only
- No room preferences/special requests

## Future Improvements
- Multi-property management
- User authentication & profiles
- Email confirmations and reminders
- SMS notifications
- Guest reviews and ratings
- Room preferences (smoking/non-smoking)
- Special requests handling
- Loyalty program
- Dynamic pricing based on demand
- Integration with payment gateways
- Cancellation policy management
- Guest service requests
- Housekeeping management
- Online payment integration
