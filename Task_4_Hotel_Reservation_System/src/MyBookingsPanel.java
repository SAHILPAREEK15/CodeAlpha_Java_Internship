import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MyBookingsPanel extends JPanel implements HotelReservationApp.RefreshablePanel {
    private HotelManager hotelManager;
    private HotelReservationApp mainApp;
    private JTextField emailField;
    private JTable bookingsTable;
    private DefaultTableModel tableModel;
    
    public MyBookingsPanel(HotelManager hotelManager, HotelReservationApp mainApp) {
        this.hotelManager = hotelManager;
        this.mainApp = mainApp;
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        searchPanel.add(new JLabel("Enter Email to view bookings:"));
        emailField = new JTextField(20);
        searchPanel.add(emailField);
        
        JButton searchBtn = new JButton("Search");
        searchBtn.setBackground(new Color(52, 152, 219));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        searchBtn.addActionListener(e -> searchBookings());
        searchPanel.add(searchBtn);
        
        add(searchPanel, BorderLayout.NORTH);
        
        // Results panel
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBackground(new Color(245, 245, 245));
        resultsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        String[] columnNames = {"Booking ID", "Room", "Check-in", "Check-out", "Total", "Status", "Action"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };
        
        bookingsTable = new JTable(tableModel);
        bookingsTable.setRowHeight(35);
        bookingsTable.setBackground(Color.WHITE);
        bookingsTable.getTableHeader().setBackground(new Color(52, 73, 120));
        bookingsTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        resultsPanel.add(scrollPane, BorderLayout.CENTER);
        add(resultsPanel, BorderLayout.CENTER);
    }
    
    private void searchBookings() {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter email!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        tableModel.setRowCount(0);
        List<Booking> bookings = hotelManager.getBookingsByEmail(email);
        
        if (bookings.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No bookings found for this email!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        for (Booking booking : bookings) {
            tableModel.addRow(new Object[]{
                booking.getBookingId(),
                booking.getRoomNumber(),
                booking.getCheckInString(),
                booking.getCheckOutString(),
                "₹" + booking.getTotalPrice(),
                booking.getStatus(),
                booking.getStatus().equals("Confirmed") ? "Cancel" : "N/A"
            });
        }
    }
    
    @Override
    public void refresh() {
        // Auto-refresh if email is filled
        if (!emailField.getText().isEmpty()) {
            searchBookings();
        }
    }
}

class AdminPanel extends JPanel implements HotelReservationApp.RefreshablePanel {
    private HotelManager hotelManager;
    private JLabel totalRoomsLabel;
    private JLabel availableLabel;
    private JLabel bookedLabel;
    private JLabel revenueLabel;
    private JTable allBookingsTable;
    private DefaultTableModel tableModel;
    
    public AdminPanel(HotelManager hotelManager) {
        this.hotelManager = hotelManager;
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        
        // Statistics panel
        JPanel statsPanel = createStatsPanel();
        add(statsPanel, BorderLayout.NORTH);
        
        // Bookings panel
        JPanel bookingsPanel = createBookingsPanel();
        add(bookingsPanel, BorderLayout.CENTER);
    }
    
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 20, 0));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        totalRoomsLabel = createStatCard("Total Rooms", "0", new Color(52, 152, 219));
        availableLabel = createStatCard("Available", "0", new Color(46, 204, 113));
        bookedLabel = createStatCard("Bookings", "0", new Color(155, 89, 182));
        revenueLabel = createStatCard("Total Revenue", "₹0", new Color(230, 126, 34));
        
        panel.add(totalRoomsLabel);
        panel.add(availableLabel);
        panel.add(bookedLabel);
        panel.add(revenueLabel);
        
        return panel;
    }
    
    private JLabel createStatCard(String title, String value, Color color) {
        JLabel label = new JLabel();
        label.setBackground(color);
        label.setOpaque(true);
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        label.setText("<html><div style='text-align: center;'><b>" + title + "</b><br>" + value + "</div></html>");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        return label;
    }
    
    private JPanel createBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel titleLabel = new JLabel("All Bookings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        String[] columnNames = {"Booking ID", "Guest", "Room", "Check-in", "Check-out", "Total", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        allBookingsTable = new JTable(tableModel);
        allBookingsTable.setRowHeight(30);
        allBookingsTable.setBackground(Color.WHITE);
        allBookingsTable.getTableHeader().setBackground(new Color(52, 73, 120));
        allBookingsTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(allBookingsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    @Override
    public void refresh() {
        // Update statistics
        totalRoomsLabel.setText("<html><div style='text-align: center;'><b>Total Rooms</b><br>" + hotelManager.getTotalRooms() + "</div></html>");
        
        java.util.Date today = new java.util.Date();
        availableLabel.setText("<html><div style='text-align: center;'><b>Available</b><br>" + hotelManager.getAvailableRoomCount(today, today) + "</div></html>");
        bookedLabel.setText("<html><div style='text-align: center;'><b>Bookings</b><br>" + hotelManager.getConfirmedBookingCount() + "</div></html>");
        revenueLabel.setText("<html><div style='text-align: center;'><b>Total Revenue</b><br>₹" + String.format("%.0f", hotelManager.getTotalRevenue()) + "</div></html>");
        
        // Update bookings table
        tableModel.setRowCount(0);
        for (Booking booking : hotelManager.getAllBookings()) {
            tableModel.addRow(new Object[]{
                booking.getBookingId(),
                booking.getGuestName(),
                booking.getRoomNumber(),
                booking.getCheckInString(),
                booking.getCheckOutString(),
                "₹" + booking.getTotalPrice(),
                booking.getStatus()
            });
        }
    }
}
