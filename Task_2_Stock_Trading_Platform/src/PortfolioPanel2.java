import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class PortfolioPanel2 extends JPanel {
    private MarketManager marketManager;
    private StockTradingApp mainApp;
    private JTable holdingsTable;
    private DefaultTableModel tableModel;
    
    public PortfolioPanel2(MarketManager marketManager, StockTradingApp mainApp) {
        this.marketManager = marketManager;
        this.mainApp = mainApp;
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel titleLabel = new JLabel("Portfolio");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(25, 42, 86));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        String[] columnNames = {"Symbol", "Company", "Quantity", "Current Price", "Value", "Action"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };
        
        holdingsTable = new JTable(tableModel);
        holdingsTable.setRowHeight(35);
        holdingsTable.setBackground(Color.WHITE);
        holdingsTable.getTableHeader().setBackground(new Color(52, 73, 120));
        holdingsTable.getTableHeader().setForeground(Color.WHITE);
        holdingsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(holdingsTable);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(contentPanel, BorderLayout.CENTER);
        
        holdingsTable.getColumnModel().getColumn(5).setCellRenderer(new SellButtonRenderer());
        holdingsTable.getColumnModel().getColumn(5).setCellEditor(new SellButtonEditor(marketManager, mainApp, this));
    }
    
    public void refresh() {
        Portfolio portfolio = marketManager.getPortfolio();
        Map<String, Stock> stocks = marketManager.getAllStocks();
        Map<String, Integer> holdings = portfolio.getHoldings();
        
        tableModel.setRowCount(0);
        
        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
            Stock stock = stocks.get(entry.getKey());
            if (stock != null) {
                double value = stock.getCurrentPrice() * entry.getValue();
                tableModel.addRow(new Object[]{
                    stock.getSymbol(),
                    stock.getCompanyName(),
                    entry.getValue(),
                    String.format("₹%.2f", stock.getCurrentPrice()),
                    String.format("₹%.2f", value),
                    "Sell"
                });
            }
        }
    }
    
    private static class SellButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public SellButtonRenderer() {
            setOpaque(true);
        }
        
        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Sell");
            setBackground(new Color(231, 76, 60));
            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.BOLD, 12));
            return this;
        }
    }
    
    private static class SellButtonEditor extends DefaultCellEditor {
        private MarketManager marketManager;
        private StockTradingApp mainApp;
        private PortfolioPanel2 panel;
        
        public SellButtonEditor(MarketManager marketManager, StockTradingApp mainApp, PortfolioPanel2 panel) {
            super(new JTextField());
            this.marketManager = marketManager;
            this.mainApp = mainApp;
            this.panel = panel;
        }
        
        @Override
        public java.awt.Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            JButton btn = new JButton("Sell");
            btn.setBackground(new Color(231, 76, 60));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Arial", Font.BOLD, 12));
            btn.setFocusPainted(false);
            
            String symbol = (String) table.getValueAt(row, 0);
            btn.addActionListener(e -> {
                Stock stock = marketManager.getStock(symbol);
                if (stock != null) {
                    SellDialog dialog = new SellDialog((JFrame) SwingUtilities.getWindowAncestor(panel),
                        marketManager, stock, mainApp, panel);
                    dialog.setVisible(true);
                }
            });
            
            return btn;
        }
    }
}
