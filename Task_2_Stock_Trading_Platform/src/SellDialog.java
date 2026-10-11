import javax.swing.*;

public class SellDialog extends JDialog {
    private Stock stock;
    private MarketManager marketManager;
    private StockTradingApp mainApp;
    
    private JSpinner quantitySpinner;
    private JLabel totalLabel;
    
    public SellDialog(JFrame parent, MarketManager marketManager, Stock stock, StockTradingApp mainApp, PortfolioPanel2 panel) {
        super(parent, "Sell " + stock.getSymbol(), true);
        this.stock = stock;
        this.marketManager = marketManager;
        this.mainApp = mainApp;
        
        initializeUI();
        setVisible(true);
    }
    
    private void initializeUI() {
        setSize(400, 300);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(java.awt.Color.WHITE);
        
        JLabel titleLabel = new JLabel("Sell " + stock.getSymbol());
        titleLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        Portfolio portfolio = marketManager.getPortfolio();
        int owned = portfolio.getHolding(stock.getSymbol());
        
        JLabel ownedLabel = new JLabel("Owned: " + owned + " shares");
        ownedLabel.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
        contentPanel.add(ownedLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        
        JLabel priceLabel = new JLabel("Current Price: ₹" + String.format("%.2f", stock.getCurrentPrice()));
        priceLabel.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
        contentPanel.add(priceLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        
        JPanel qtyPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        qtyPanel.setBackground(java.awt.Color.WHITE);
        JLabel qtyLabel = new JLabel("Quantity:");
        qtyLabel.setPreferredSize(new java.awt.Dimension(80, 25));
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, owned > 0 ? owned : 1, 1));
        quantitySpinner.addChangeListener(e -> updateTotal());
        qtyPanel.add(qtyLabel);
        qtyPanel.add(quantitySpinner);
        contentPanel.add(qtyPanel);
        contentPanel.add(Box.createVerticalStrut(10));
        
        totalLabel = new JLabel("Total: ₹0.00");
        totalLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        contentPanel.add(totalLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        JPanel buttonPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(java.awt.Color.WHITE);
        
        JButton sellBtn = new JButton("Sell Now");
        sellBtn.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        sellBtn.setBackground(new java.awt.Color(231, 76, 60));
        sellBtn.setForeground(java.awt.Color.WHITE);
        sellBtn.setFocusPainted(false);
        sellBtn.setPreferredSize(new java.awt.Dimension(100, 35));
        sellBtn.addActionListener(e -> executeSell());
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        cancelBtn.setBackground(new java.awt.Color(149, 165, 166));
        cancelBtn.setForeground(java.awt.Color.WHITE);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setPreferredSize(new java.awt.Dimension(100, 35));
        cancelBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(sellBtn);
        buttonPanel.add(cancelBtn);
        contentPanel.add(buttonPanel);
        
        add(contentPanel);
        updateTotal();
    }
    
    private void updateTotal() {
        int quantity = (int) quantitySpinner.getValue();
        double total = quantity * stock.getCurrentPrice();
        totalLabel.setText("Total: ₹" + String.format("%.2f", total));
    }
    
    private void executeSell() {
        int quantity = (int) quantitySpinner.getValue();
        
        if (marketManager.sellStock(stock.getSymbol(), quantity)) {
            JOptionPane.showMessageDialog(this, 
                "Successfully sold " + quantity + " shares of " + stock.getSymbol(), 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            mainApp.repaint();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Error: Cannot sell these shares", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
