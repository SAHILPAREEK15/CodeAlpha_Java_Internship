import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class HotelReservationApp extends JFrame {
    private HotelManager hotelManager;
    private JTabbedPane tabbedPane;
    
    public HotelReservationApp() {
        hotelManager = new HotelManager();
        initializeUI();
    }
    
    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Royal Stay Hotel - Room Booking System");
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Top bar
        JPanel topBar = createTopBar();
        add(topBar, BorderLayout.NORTH);
        
        // Tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Home", new HomePanel(hotelManager, this));
        tabbedPane.addTab("Search Rooms", new SearchRoomsPanel(hotelManager, this));
        tabbedPane.addTab("My Bookings", new MyBookingsPanel(hotelManager, this));
        tabbedPane.addTab("Admin", new AdminPanel(hotelManager));
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(139, 69, 19)); // Brown color for hotel theme
        topBar.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        JLabel hotelName = new JLabel("Royal Stay Hotel");
        hotelName.setFont(new Font("Arial", Font.BOLD, 24));
        hotelName.setForeground(Color.WHITE);
        
        JLabel tagline = new JLabel("Your Comfort, Our Priority");
        tagline.setFont(new Font("Arial", Font.ITALIC, 12));
        tagline.setForeground(new Color(255, 200, 100));
        
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(139, 69, 19));
        leftPanel.add(hotelName);
        leftPanel.add(tagline);
        
        topBar.add(leftPanel, BorderLayout.WEST);
        
        JLabel contactLabel = new JLabel("📞 +91-9876543210 | 📧 info@royalstay.com");
        contactLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        contactLabel.setForeground(Color.WHITE);
        topBar.add(contactLabel, BorderLayout.EAST);
        
        return topBar;
    }
    
    public void refreshAllTabs() {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            Component comp = tabbedPane.getComponentAt(i);
            if (comp instanceof RefreshablePanel) {
                ((RefreshablePanel) comp).refresh();
            }
        }
    }
    
    interface RefreshablePanel {
        void refresh();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HotelReservationApp app = new HotelReservationApp();
            app.setVisible(true);
        });
    }
}
