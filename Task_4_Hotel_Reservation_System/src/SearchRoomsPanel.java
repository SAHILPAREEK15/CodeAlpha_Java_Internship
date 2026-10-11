import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SearchRoomsPanel extends JPanel implements HotelReservationApp.RefreshablePanel {
    private HotelManager hotelManager;
    private HotelReservationApp mainApp;
    private JSpinner checkInSpinner;
    private JSpinner checkOutSpinner;
    private JSpinner guestsSpinner;
    private JComboBox<String> categoryCombo;
    private JTable roomsTable;
    private DefaultTableModel tableModel;
    
    public SearchRoomsPanel(HotelManager hotelManager, HotelReservationApp mainApp) {
        this.hotelManager = hotelManager;
        this.mainApp = mainApp;
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        
        // Search panel
        JPanel searchPanel = createSearchPanel();
        add(searchPanel, BorderLayout.NORTH);
        
        // Results panel
        JPanel resultsPanel = createResultsPanel();
        add(resultsPanel, BorderLayout.CENTER);
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Check-in date
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Check-in:"), gbc);
        checkInSpinner = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
        checkInSpinner.setEditor(new JSpinner.DateEditor(checkInSpinner, "dd/MM/yyyy"));
        gbc.gridx = 1;
        panel.add(checkInSpinner, gbc);
        
        // Check-out date
        gbc.gridx = 2;
        gbc.gridy = 0;
        panel.add(new JLabel("Check-out:"), gbc);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        checkOutSpinner = new JSpinner(new SpinnerDateModel(cal.getTime(), null, null, Calendar.DAY_OF_MONTH));
        checkOutSpinner.setEditor(new JSpinner.DateEditor(checkOutSpinner, "dd/MM/yyyy"));
        gbc.gridx = 3;
        panel.add(checkOutSpinner, gbc);
        
        // Guests
        gbc.gridx = 4;
        gbc.gridy = 0;
        panel.add(new JLabel("Guests:"), gbc);
        guestsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        gbc.gridx = 5;
        panel.add(guestsSpinner, gbc);
        
        // Category
        gbc.gridx = 6;
        gbc.gridy = 0;
        panel.add(new JLabel("Category:"), gbc);
        categoryCombo = new JComboBox<>(new String[]{"All", "Standard", "Deluxe", "Suite", "Premium"});
        gbc.gridx = 7;
        panel.add(categoryCombo, gbc);
        
        // Search button
        JButton searchBtn = new JButton("Search");
        searchBtn.setFont(new Font("Arial", Font.BOLD, 12));
        searchBtn.setBackground(new Color(52, 152, 219));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        searchBtn.addActionListener(e -> search());
        gbc.gridx = 8;
        gbc.gridy = 0;
        panel.add(searchBtn, gbc);
        
        return panel;
    }
    
    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        String[] columnNames = {"Room #", "Category", "Price/Night", "Capacity", "Status", "Book"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };
        
        roomsTable = new JTable(tableModel);
        roomsTable.setRowHeight(35);
        roomsTable.setBackground(Color.WHITE);
        roomsTable.getTableHeader().setBackground(new Color(52, 73, 120));
        roomsTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(roomsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        roomsTable.getColumnModel().getColumn(5).setCellRenderer(new BookButtonRenderer());
        roomsTable.getColumnModel().getColumn(5).setCellEditor(new BookButtonEditor(hotelManager, mainApp, this));
        
        return panel;
    }
    
    private void search() {
        Date checkIn = (Date) checkInSpinner.getValue();
        Date checkOut = (Date) checkOutSpinner.getValue();
        int guests = (int) guestsSpinner.getValue();
        String selectedCategory = (String) categoryCombo.getSelectedItem();
        
        tableModel.setRowCount(0);
        
        List<Room> results;
        if ("All".equals(selectedCategory)) {
            results = hotelManager.getAvailableRooms(checkIn, checkOut);
        } else {
            Room.RoomCategory category = Room.RoomCategory.valueOf(selectedCategory.toUpperCase());
            results = hotelManager.getAvailableRoomsByCategory(category, checkIn, checkOut);
        }
        
        for (Room room : results) {
            tableModel.addRow(new Object[]{
                room.getRoomNumber(),
                room.getCategory().getDisplayName(),
                "₹" + room.getPricePerNight(),
                room.getCapacity(),
                room.isAvailable() ? "Available" : "Booked",
                "Book"
            });
        }
    }
    
    @Override
    public void refresh() {
        search();
    }
    
    private static class BookButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public BookButtonRenderer() {
            setOpaque(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Book");
            setBackground(new Color(46, 204, 113));
            setForeground(Color.WHITE);
            return this;
        }
    }
    
    private static class BookButtonEditor extends DefaultCellEditor {
        private HotelManager hotelManager;
        private HotelReservationApp mainApp;
        private SearchRoomsPanel panel;
        
        public BookButtonEditor(HotelManager hotelManager, HotelReservationApp mainApp, SearchRoomsPanel panel) {
            super(new JTextField());
            this.hotelManager = hotelManager;
            this.mainApp = mainApp;
            this.panel = panel;
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            JButton btn = new JButton("Book");
            btn.setBackground(new Color(46, 204, 113));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            
            String roomNumber = (String) table.getValueAt(row, 0);
            btn.addActionListener(e -> {
                Date checkIn = (Date) panel.checkInSpinner.getValue();
                Date checkOut = (Date) panel.checkOutSpinner.getValue();
                int guests = (int) panel.guestsSpinner.getValue();
                Room room = hotelManager.getRoom(roomNumber);
                
                BookingDialog dialog = new BookingDialog((JFrame) SwingUtilities.getWindowAncestor(panel),
                    hotelManager, room, checkIn, checkOut, guests, mainApp);
                dialog.setVisible(true);
            });
            
            return btn;
        }
    }
}
