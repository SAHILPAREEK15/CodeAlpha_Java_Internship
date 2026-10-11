import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class MarketPanel2 extends JPanel {
    private MarketManager marketManager;
    private StockTradingApp mainApp;
    private JTextField searchField;
    private JTable marketTable;
    private DefaultTableModel tableModel;
    
    public MarketPanel2(MarketManager marketManager, StockTradingApp mainApp) {
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
        
        JLabel titleLabel = new JLabel("Market");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(25, 42, 86));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        searchPanel.add(searchLabel);
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 12));
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                search();
            }
        });
        searchPanel.add(searchField);
        
        add(searchPanel, BorderLayout.NORTH);
        
        // Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(245, 245, 245));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));
        
        String[] columnNames = {"Symbol", "Company Name", "Current Price", "Previous Price", "Change", "% Change", "Action"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };
        
        marketTable = new JTable(tableModel);
        marketTable.setRowHeight(35);
        marketTable.setBackground(Color.WHITE);
        marketTable.getTableHeader().setBackground(new Color(52, 73, 120));
        marketTable.getTableHeader().setForeground(Color.WHITE);
        marketTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(marketTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        add(tablePanel, BorderLayout.CENTER);
        
        marketTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        marketTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(marketManager, mainApp, this));
    }
    
    private void search() {
        String query = searchField.getText().trim();
        List<Stock> results = query.isEmpty() ? 
            marketManager.searchStocks("") : 
            marketManager.searchStocks(query);
        
        updateTable(results);
    }
    
    private void updateTable(List<Stock> stocks) {
        tableModel.setRowCount(0);
        
        if (stocks.isEmpty()) {
            stocks = marketManager.getAllStocks().values().stream().toList();
        }
        
        for (Stock stock : stocks) {
            String changeColor = stock.getPriceChange() >= 0 ? "↑" : "↓";
            tableModel.addRow(new Object[]{
                stock.getSymbol(),
                stock.getCompanyName(),
                String.format("₹%.2f", stock.getCurrentPrice()),
                String.format("₹%.2f", stock.getPreviousPrice()),
                String.format("%s ₹%.2f", changeColor, Math.abs(stock.getPriceChange())),
                String.format("%s%.2f%%", stock.getPercentageChange() >= 0 ? "+" : "", stock.getPercentageChange()),
                "Buy"
            });
        }
    }
    
    public void refresh() {
        search();
    }
    
    private static class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        
        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Buy");
            setBackground(new Color(46, 204, 113));
            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.BOLD, 12));
            setFocusPainted(false);
            return this;
        }
    }
    
    private static class ButtonEditor extends DefaultCellEditor {
        private MarketManager marketManager;
        private StockTradingApp mainApp;
        private MarketPanel2 panel;
        
        public ButtonEditor(MarketManager marketManager, StockTradingApp mainApp, MarketPanel2 panel) {
            super(new JTextField());
            this.marketManager = marketManager;
            this.mainApp = mainApp;
            this.panel = panel;
        }
        
        @Override
        public java.awt.Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            JButton btn = new JButton("Buy");
            btn.setBackground(new Color(46, 204, 113));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Arial", Font.BOLD, 12));
            btn.setFocusPainted(false);
            
            String symbol = (String) table.getValueAt(row, 0);
            btn.addActionListener(e -> {
                Stock stock = marketManager.getStock(symbol);
                if (stock != null) {
                    BuyDialog dialog = new BuyDialog((JFrame) SwingUtilities.getWindowAncestor(panel),
                        marketManager, stock, mainApp, panel);
                    dialog.setVisible(true);
                }
            });
            
            return btn;
        }
    }
}
