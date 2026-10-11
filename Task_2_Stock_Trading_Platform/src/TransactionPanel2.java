import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TransactionPanel2 extends JPanel {
    private MarketManager marketManager;
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    
    public TransactionPanel2(MarketManager marketManager) {
        this.marketManager = marketManager;
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel titleLabel = new JLabel("Transactions");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(25, 42, 86));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        String[] columnNames = {"Date/Time", "Type", "Symbol", "Quantity", "Price", "Total"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        transactionTable = new JTable(tableModel);
        transactionTable.setRowHeight(30);
        transactionTable.setBackground(Color.WHITE);
        transactionTable.getTableHeader().setBackground(new Color(52, 73, 120));
        transactionTable.getTableHeader().setForeground(Color.WHITE);
        transactionTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(contentPanel, BorderLayout.CENTER);
    }
    
    public void refresh() {
        Portfolio portfolio = marketManager.getPortfolio();
        List<Portfolio.Transaction> transactions = portfolio.getTransactionHistory();
        
        tableModel.setRowCount(0);
        for (Portfolio.Transaction tx : transactions) {
            tableModel.addRow(new Object[]{
                tx.getDateString(),
                tx.getType(),
                tx.getSymbol(),
                tx.getQuantity(),
                String.format("₹%.2f", tx.getPrice()),
                String.format("₹%.2f", tx.getTotal())
            });
        }
    }
}
