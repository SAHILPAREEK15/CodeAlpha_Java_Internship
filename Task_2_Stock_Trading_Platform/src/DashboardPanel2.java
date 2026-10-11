import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class DashboardPanel2 extends JPanel {
    private MarketManager marketManager;
    private StockTradingApp mainApp;
    private JLabel portfolioValueLabel;
    private JLabel todayPLLabel;
    private JLabel balanceLabel;
    private JLabel holdingsLabel;
    private JTable watchlistTable;
    
    public DashboardPanel2(MarketManager marketManager, StockTradingApp mainApp) {
        this.marketManager = marketManager;
        this.mainApp = mainApp;
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(25, 42, 86));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Stats cards
        JPanel statsPanel = createStatsPanel();
        contentPanel.add(statsPanel);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Watchlist section
        JPanel watchlistPanel = createWatchlistPanel();
        contentPanel.add(watchlistPanel);
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBackground(new Color(245, 245, 245));
        scrollPane.getViewport().setBackground(new Color(245, 245, 245));
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 20, 0));
        panel.setBackground(new Color(245, 245, 245));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        
        portfolioValueLabel = createStatCard("Portfolio Value", "₹0", new Color(52, 152, 219));
        todayPLLabel = createStatCard("Today's P/L", "₹0", new Color(46, 204, 113));
        balanceLabel = createStatCard("Available Balance", "₹0", new Color(155, 89, 182));
        holdingsLabel = createStatCard("Total Holdings", "0", new Color(230, 126, 34));
        
        panel.add(portfolioValueLabel);
        panel.add(todayPLLabel);
        panel.add(balanceLabel);
        panel.add(holdingsLabel);
        
        return panel;
    }
    
    private JLabel createStatCard(String title, String value, Color bgColor) {
        JLabel label = new JLabel();
        label.setBackground(bgColor);
        label.setOpaque(true);
        label.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        label.setForeground(Color.WHITE);
        
        Box box = Box.createVerticalBox();
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(Color.WHITE);
        
        box.add(titleLabel);
        box.add(Box.createVerticalStrut(10));
        box.add(valueLabel);
        
        label.setText("");
        return label;
    }
    
    private JPanel createWatchlistPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Market Overview");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(25, 42, 86));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        String[] columnNames = {"Symbol", "Company", "Current Price", "Change", "% Change"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        watchlistTable = new JTable(model);
        watchlistTable.setRowHeight(30);
        watchlistTable.setBackground(Color.WHITE);
        watchlistTable.getTableHeader().setBackground(new Color(52, 73, 120));
        watchlistTable.getTableHeader().setForeground(Color.WHITE);
        watchlistTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(watchlistTable);
        scrollPane.setPreferredSize(new Dimension(0, 250));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    public void refresh() {
        Portfolio portfolio = marketManager.getPortfolio();
        Map<String, Stock> stocks = marketManager.getAllStocks();
        
        double portfolioValue = portfolio.getPortfolioValue(stocks);
        double pl = portfolio.getProfitLoss(stocks);
        
        updateStatCard(portfolioValueLabel, "Portfolio Value", String.format("₹%.2f", portfolioValue));
        updateStatCard(todayPLLabel, "Today's P/L", String.format("₹%.2f", pl));
        updateStatCard(balanceLabel, "Available Balance", String.format("₹%.2f", portfolio.getBalance()));
        updateStatCard(holdingsLabel, "Total Holdings", String.valueOf(portfolio.getHoldings().size()));
        
        // Update market table
        DefaultTableModel model = (DefaultTableModel) watchlistTable.getModel();
        model.setRowCount(0);
        
        for (Stock stock : stocks.values()) {
            String changeColor = stock.getPriceChange() >= 0 ? "↑" : "↓";
            model.addRow(new Object[]{
                stock.getSymbol(),
                stock.getCompanyName(),
                String.format("₹%.2f", stock.getCurrentPrice()),
                String.format("%s ₹%.2f", changeColor, Math.abs(stock.getPriceChange())),
                String.format("%s%.2f%%", stock.getPercentageChange() >= 0 ? "+" : "", stock.getPercentageChange())
            });
        }
    }
    
    private void updateStatCard(JLabel card, String title, String value) {
        card.setText("");
        card.removeAll();
        
        Box box = Box.createVerticalBox();
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(Color.WHITE);
        
        box.add(titleLabel);
        box.add(Box.createVerticalStrut(10));
        box.add(valueLabel);
        
        card.add(box);
        card.revalidate();
        card.repaint();
    }
}
