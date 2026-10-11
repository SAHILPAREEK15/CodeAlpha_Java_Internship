import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class StockTradingApp extends JFrame {
    private MarketManager marketManager;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel sidebarPanel;
    
    private DashboardPanel2 dashboardPanel;
    private MarketPanel2 marketPanel;
    private WatchlistPanel2 watchlistPanel;
    private PortfolioPanel2 portfolioPanel;
    private TransactionPanel2 transactionPanel;
    
    private Timer marketUpdateTimer;
    
    public StockTradingApp() {
        marketManager = new MarketManager();
        initializeUI();
        startMarketUpdate();
    }
    
    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("StockTrade - Simulated Stock Trading Platform");
        setSize(1600, 900);
        setLocationRelativeTo(null);
        setResizable(true);
        
        JPanel container = new JPanel(new BorderLayout());
        
        // Sidebar
        sidebarPanel = createSidebar();
        container.add(sidebarPanel, BorderLayout.WEST);
        
        // Top bar with info
        JPanel topBar = createTopBar();
        container.add(topBar, BorderLayout.NORTH);
        
        // Main content area with card layout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        dashboardPanel = new DashboardPanel2(marketManager, this);
        marketPanel = new MarketPanel2(marketManager, this);
        watchlistPanel = new WatchlistPanel2(marketManager, this);
        portfolioPanel = new PortfolioPanel2(marketManager, this);
        transactionPanel = new TransactionPanel2(marketManager);
        
        mainPanel.add(dashboardPanel, "Dashboard");
        mainPanel.add(marketPanel, "Market");
        mainPanel.add(watchlistPanel, "Watchlist");
        mainPanel.add(portfolioPanel, "Portfolio");
        mainPanel.add(transactionPanel, "Transactions");
        
        container.add(mainPanel, BorderLayout.CENTER);
        
        add(container);
    }
    
    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(20, 33, 61));
        topBar.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        JLabel titleLabel = new JLabel("StockTrade | Simulated Market");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(46, 204, 113));
        topBar.add(titleLabel, BorderLayout.WEST);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 0));
        rightPanel.setBackground(new Color(20, 33, 61));
        
        JLabel statusLabel = new JLabel("Status: LIVE TRADING");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        statusLabel.setForeground(new Color(46, 204, 113));
        
        JButton refreshBtn = new JButton("Refresh Market");
        refreshBtn.setFont(new Font("Arial", Font.PLAIN, 11));
        refreshBtn.setBackground(new Color(52, 152, 219));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.addActionListener(e -> {
            marketManager.updateMarketPrices();
            refreshAllPanels();
        });
        
        rightPanel.add(statusLabel);
        rightPanel.add(refreshBtn);
        topBar.add(rightPanel, BorderLayout.EAST);
        
        return topBar;
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(25, 42, 86));
        sidebar.setPreferredSize(new Dimension(180, 0));
        
        // Header
        JLabel headerLabel = new JLabel("StockTrade");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        sidebar.add(headerLabel);
        sidebar.add(Box.createVerticalStrut(30));
        
        // Navigation buttons
        String[] menuItems = {"Dashboard", "Market", "Watchlist", "Portfolio", "Transactions"};
        String[] actionNames = {"Dashboard", "Market", "Watchlist", "Portfolio", "Transactions"};
        
        for (int i = 0; i < menuItems.length; i++) {
            JButton btn = createNavButton(menuItems[i], actionNames[i]);
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(10));
        }
        
        sidebar.add(Box.createVerticalGlue());
        
        // Footer
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setMaximumSize(new Dimension(160, 40));
        logoutBtn.setBackground(new Color(220, 53, 69));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 12));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        logoutBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this, 
                "Exit Stock Trading Platform?", 
                "Confirm Exit", 
                JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(logoutBtn);
        sidebar.add(Box.createVerticalStrut(10));
        
        return sidebar;
    }
    
    private JButton createNavButton(String text, String actionName) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(160, 45));
        btn.setBackground(new Color(52, 73, 120));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.PLAIN, 12));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(65, 95, 154));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(52, 73, 120));
            }
        });
        
        btn.addActionListener(e -> {
            cardLayout.show(mainPanel, actionName);
            refreshAllPanels();
        });
        
        return btn;
    }
    
    private void refreshAllPanels() {
        dashboardPanel.refresh();
        marketPanel.refresh();
        watchlistPanel.refresh();
        portfolioPanel.refresh();
        transactionPanel.refresh();
    }
    
    private void startMarketUpdate() {
        marketUpdateTimer = new Timer(5000, e -> {
            marketManager.updateMarketPrices();
            refreshAllPanels();
        });
        marketUpdateTimer.start();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StockTradingApp app = new StockTradingApp();
            app.setVisible(true);
        });
    }
}
