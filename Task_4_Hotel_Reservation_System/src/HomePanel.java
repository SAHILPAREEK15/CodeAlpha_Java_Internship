import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomePanel extends JPanel implements HotelReservationApp.RefreshablePanel {
    private HotelManager hotelManager;
    private HotelReservationApp mainApp;
    
    public HomePanel(HotelManager hotelManager, HotelReservationApp mainApp) {
        this.hotelManager = hotelManager;
        this.mainApp = mainApp;
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        
        // Hero section
        JPanel heroPanel = createHeroPanel();
        add(heroPanel, BorderLayout.NORTH);
        
        // Content area
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));
        
        // Welcome section
        JLabel welcomeLabel = new JLabel("Welcome to Royal Stay Hotel");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(25, 42, 86));
        contentPanel.add(welcomeLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Features section
        JPanel featuresPanel = createFeaturesPanel();
        contentPanel.add(featuresPanel);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Room categories section
        JPanel categoriesPanel = createCategoriesPanel();
        contentPanel.add(categoriesPanel);
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBackground(new Color(245, 245, 245));
        scrollPane.getViewport().setBackground(new Color(245, 245, 245));
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createHeroPanel() {
        JPanel heroPanel = new JPanel();
        heroPanel.setLayout(new BorderLayout());
        heroPanel.setBackground(new Color(100, 150, 200));
        heroPanel.setPreferredSize(new Dimension(0, 200));
        heroPanel.setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));
        
        JLabel heroTitle = new JLabel("Find Your Perfect Stay");
        heroTitle.setFont(new Font("Arial", Font.BOLD, 32));
        heroTitle.setForeground(Color.WHITE);
        
        JLabel heroSubtitle = new JLabel("Comfortable rooms • Best rates • Unforgettable experience");
        heroSubtitle.setFont(new Font("Arial", Font.ITALIC, 14));
        heroSubtitle.setForeground(new Color(255, 200, 100));
        
        Box vBox = Box.createVerticalBox();
        vBox.add(heroTitle);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(heroSubtitle);
        
        heroPanel.add(vBox, BorderLayout.WEST);
        
        JButton searchBtn = new JButton("Search Rooms");
        searchBtn.setFont(new Font("Arial", Font.BOLD, 14));
        searchBtn.setBackground(new Color(139, 69, 19));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        searchBtn.setPreferredSize(new Dimension(150, 45));
        searchBtn.addActionListener(e -> mainApp.tabbedPane.setSelectedIndex(1));
        
        heroPanel.add(searchBtn, BorderLayout.EAST);
        
        return heroPanel;
    }
    
    private JPanel createFeaturesPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 20, 0));
        panel.setBackground(new Color(245, 245, 245));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        String[] features = {"Free WiFi", "Room Service", "Swimming Pool", "24/7 Support"};
        Color[] colors = {
            new Color(52, 152, 219),
            new Color(46, 204, 113),
            new Color(155, 89, 182),
            new Color(230, 126, 34)
        };
        
        for (int i = 0; i < features.length; i++) {
            JPanel featureCard = new JPanel();
            featureCard.setBackground(colors[i]);
            featureCard.setLayout(new BorderLayout());
            featureCard.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            
            JLabel featureLabel = new JLabel(features[i]);
            featureLabel.setFont(new Font("Arial", Font.BOLD, 14));
            featureLabel.setForeground(Color.WHITE);
            featureLabel.setHorizontalAlignment(JLabel.CENTER);
            
            featureCard.add(featureLabel, BorderLayout.CENTER);
            panel.add(featureCard);
        }
        
        return panel;
    }
    
    private JPanel createCategoriesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 20, 20));
        panel.setBackground(new Color(245, 245, 245));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));
        
        Room.RoomCategory[] categories = {
            Room.RoomCategory.STANDARD,
            Room.RoomCategory.DELUXE,
            Room.RoomCategory.SUITE,
            Room.RoomCategory.PREMIUM
        };
        
        Color[] bgColors = {
            new Color(52, 152, 219),
            new Color(46, 204, 113),
            new Color(155, 89, 182),
            new Color(230, 126, 34)
        };
        
        for (int i = 0; i < categories.length; i++) {
            JPanel categoryCard = createCategoryCard(categories[i], bgColors[i]);
            panel.add(categoryCard);
        }
        
        return panel;
    }
    
    private JPanel createCategoryCard(Room.RoomCategory category, Color bgColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel categoryLabel = new JLabel(category.getDisplayName());
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 18));
        categoryLabel.setForeground(bgColor);
        
        JLabel priceLabel = new JLabel("₹" + category.getPricePerNight() + "/night");
        priceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        priceLabel.setForeground(new Color(100, 100, 100));
        
        JLabel description = new JLabel(getDescription(category));
        description.setFont(new Font("Arial", Font.PLAIN, 12));
        description.setForeground(new Color(100, 100, 100));
        
        card.add(categoryLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(priceLabel);
        card.add(Box.createVerticalStrut(15));
        card.add(description);
        
        JButton bookBtn = new JButton("Book Now");
        bookBtn.setFont(new Font("Arial", Font.BOLD, 12));
        bookBtn.setBackground(bgColor);
        bookBtn.setForeground(Color.WHITE);
        bookBtn.setFocusPainted(false);
        bookBtn.setMaximumSize(new Dimension(120, 35));
        bookBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookBtn.addActionListener(e -> mainApp.tabbedPane.setSelectedIndex(1));
        
        card.add(Box.createVerticalStrut(15));
        card.add(bookBtn);
        
        return card;
    }
    
    private String getDescription(Room.RoomCategory category) {
        return switch (category) {
            case STANDARD -> "Comfortable room with basic amenities";
            case DELUXE -> "Spacious room with modern facilities";
            case SUITE -> "Luxury suite with premium features";
            case PREMIUM -> "Ultimate luxury experience";
        };
    }
    
    @Override
    public void refresh() {
        // Refresh logic if needed
    }
}
