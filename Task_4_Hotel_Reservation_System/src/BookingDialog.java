import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class BookingDialog extends JDialog {
    private HotelManager hotelManager;
    private Room room;
    private Date checkIn;
    private Date checkOut;
    private int guests;
    private HotelReservationApp mainApp;
    
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JComboBox<String> paymentCombo;
    
    public BookingDialog(JFrame parent, HotelManager hotelManager, Room room, Date checkIn, 
                        Date checkOut, int guests, HotelReservationApp mainApp) {
        super(parent, "Complete Booking", true);
        this.hotelManager = hotelManager;
        this.room = room;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.guests = guests;
        this.mainApp = mainApp;
        
        initializeUI();
        setVisible(true);
    }
    
    private void initializeUI() {
        setSize(500, 500);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);
        
        // Room details
        JLabel roomLabel = new JLabel("Room: " + room.getRoomNumber() + " - " + room.getCategory().getDisplayName());
        roomLabel.setFont(new Font("Arial", Font.BOLD, 14));
        contentPanel.add(roomLabel);
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Calculate nights and total
        long diffTime = checkOut.getTime() - checkIn.getTime();
        int nights = (int) (diffTime / (1000 * 60 * 60 * 24));
        if (nights <= 0) nights = 1;
        double totalPrice = nights * room.getPricePerNight();
        
        JLabel priceLabel = new JLabel(nights + " nights @ ₹" + room.getPricePerNight() + "/night = ₹" + totalPrice);
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        contentPanel.add(priceLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Guest details
        JLabel guestLabel = new JLabel("Guest Details:");
        guestLabel.setFont(new Font("Arial", Font.BOLD, 12));
        contentPanel.add(guestLabel);
        
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.setBackground(Color.WHITE);
        namePanel.add(new JLabel("Name:"));
        nameField = new JTextField(20);
        namePanel.add(nameField);
        contentPanel.add(namePanel);
        
        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emailPanel.setBackground(Color.WHITE);
        emailPanel.add(new JLabel("Email:"));
        emailField = new JTextField(20);
        emailPanel.add(emailField);
        contentPanel.add(emailPanel);
        
        JPanel phonePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        phonePanel.setBackground(Color.WHITE);
        phonePanel.add(new JLabel("Phone:"));
        phoneField = new JTextField(20);
        phonePanel.add(phoneField);
        contentPanel.add(phonePanel);
        
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Payment method
        JLabel paymentLabel = new JLabel("Payment Method:");
        paymentLabel.setFont(new Font("Arial", Font.BOLD, 12));
        contentPanel.add(paymentLabel);
        
        paymentCombo = new JComboBox<>(new String[]{"Cash", "Card", "UPI"});
        contentPanel.add(paymentCombo);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton confirmBtn = new JButton("Confirm Booking");
        confirmBtn.setFont(new Font("Arial", Font.BOLD, 12));
        confirmBtn.setBackground(new Color(46, 204, 113));
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.setFocusPainted(false);
        confirmBtn.setPreferredSize(new Dimension(130, 35));
        confirmBtn.addActionListener(e -> confirmBooking());
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 12));
        cancelBtn.setBackground(new Color(149, 165, 166));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setPreferredSize(new Dimension(100, 35));
        cancelBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(confirmBtn);
        buttonPanel.add(cancelBtn);
        contentPanel.add(buttonPanel);
        
        add(contentPanel);
    }
    
    private void confirmBooking() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String payment = (String) paymentCombo.getSelectedItem();
        
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all guest details!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Booking booking = hotelManager.createBooking(name, email, phone, checkIn, checkOut, guests, room.getRoomNumber());
        
        if (booking != null) {
            booking.setPaymentMethod(payment);
            JOptionPane.showMessageDialog(this, 
                "Booking Confirmed!\nBooking ID: " + booking.getBookingId() + 
                "\nTotal Amount: ₹" + booking.getTotalPrice(),
                "Success", JOptionPane.INFORMATION_MESSAGE);
            mainApp.refreshAllTabs();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error creating booking!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
