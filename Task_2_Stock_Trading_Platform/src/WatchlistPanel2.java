import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WatchlistPanel2 extends JPanel {
    private MarketManager marketManager;
    private StockTradingApp mainApp;
    private JTable watchlistTable;
    private DefaultTableModel tableModel;
    private List<String> watchedStocks;
    
    public WatchlistPanel2(MarketManager marketManager, StockTradingApp mainApp) {
        this.marketManager = marketManager;
        this.mainApp = mainApp;
        this.watchedStocks = new ArrayList<>();
        watchedStocks.addAll(marketManager.getAllStocks().keySet());
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel titleLabel = new JLabel("Watchlist");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(25, 42, 86));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        String[] columnNames = {"Symbol", "Company", "Price", "Change", "% Change", "Action"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return column == 5; }
        };
        
        watchlistTable = new JTable(tableModel);
        watchlistTable.setRowHeight(35);
        watchlistTable.setBackground(Color.WHITE);
        watchlistTable.getTableHeader().setBackground(new Color(52, 73, 120));
        watchlistTable.getTableHeader().setForeground(Color.WHITE);
        watchlistTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(watchlistTable);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(contentPanel, BorderLayout.CENTER);
    }
    
    public void refresh() {
        tableModel.setRowCount(0);
        for (String symbol : watchedStocks) {
            Stock stock = marketManager.getStock(symbol);
            if (stock != null) {
                String changeColor = stock.getPriceChange() >= 0 ? "↑" : "↓";
                tableModel.addRow(new Object[]{
                    stock.getSymbol(),
                    stock.getCompanyName(),
                    String.format("₹%.2f", stock.getCurrentPrice()),
                    String.format("%s ₹%.2f", changeColor, Math.abs(stock.getPriceChange())),
                    String.format("%s%.2f%%", stock.getPercentageChange() >= 0 ? "+" : "", stock.getPercentageChange()),
                    "Remove"
                });
            }
        }
    }
}
