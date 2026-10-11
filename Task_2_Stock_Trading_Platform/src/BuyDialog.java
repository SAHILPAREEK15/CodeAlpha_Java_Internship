import javax.swing.*;

public class BuyDialog extends JDialog {
    private Stock stock;
    private MarketManager marketManager;
    private StockTradingApp mainApp;
    private MarketPanel2 panel;
    
    private JSpinner quantitySpinner;
    private JLabel totalLabel;
    
    public BuyDialog(JFrame parent, MarketManager marketManager, Stock stock, StockTradingApp mainApp, MarketPanel2 panel) {
        super(parent, "Buy " + stock.getSymbol(), true);
        this.stock = stock;
        this.marketManager = marketManager;
        this.mainApp = mainApp;
        this.panel = panel;
        
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
        
        JLabel titleLabel = new JLabel("Buy " + stock.getSymbol());
        titleLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        JLabel priceLabel = new JLabel("Current Price: ₹" + String.format("%.2f", stock.getCurrentPrice()));
        priceLabel.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
        contentPanel.add(priceLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        
        JPanel qtyPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        qtyPanel.setBackground(java.awt.Color.WHITE);
        JLabel qtyLabel = new JLabel("Quantity:");
        qtyLabel.setPreferredSize(new java.awt.Dimension(80, 25));
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        quantitySpinner.addChangeListener(e -> updateTotal());
        qtyPanel.add(qtyLabel);
        qtyPanel.add(quantitySpinner);
        contentPanel.add(qtyPanel);
        contentPanel.add(Box.createVerticalStrut(10));
        
        totalLabel = new JLabel("Total: ₹0.00");
        totalLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        contentPanel.add(totalLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        
        JLabel balanceLabel = new JLabel("Available Balance: ₹" + String.format("%.2f", marketManager.getPortfolio().getBalance()));
        balanceLabel.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 11));
        contentPanel.add(balanceLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        JPanel buttonPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(java.awt.Color.WHITE);
        
        JButton buyBtn = new JButton("Buy Now");
        buyBtn.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        buyBtn.setBackground(new java.awt.Color(46, 204, 113));
        buyBtn.setForeground(java.awt.Color.WHITE);
        buyBtn.setFocusPainted(false);
        buyBtn.setPreferredSize(new java.awt.Dimension(100, 35));
        buyBtn.addActionListener(e -> executeBuy());
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        cancelBtn.setBackground(new java.awt.Color(149, 165, 166));
        cancelBtn.setForeground(java.awt.Color.WHITE);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setPreferredSize(new java.awt.Dimension(100, 35));
        cancelBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(buyBtn);
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
    
    private void executeBuy() {
        int quantity = (int) quantitySpinner.getValue();
        
        if (marketManager.buyStock(stock.getSymbol(), quantity)) {
            JOptionPane.showMessageDialog(this, 
                "Successfully bought " + quantity + " shares of " + stock.getSymbol(), 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            mainApp.repaint();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Insufficient balance to complete this transaction", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
