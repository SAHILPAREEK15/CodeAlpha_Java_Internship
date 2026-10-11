import java.io.Serializable;
import java.util.*;

public class Portfolio implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Map<String, Integer> holdings; // symbol -> quantity
    private List<Transaction> transactionHistory;
    private double balance;
    private double initialBalance;
    
    public Portfolio(double initialBalance) {
        this.initialBalance = initialBalance;
        this.balance = initialBalance;
        this.holdings = new HashMap<>();
        this.transactionHistory = new ArrayList<>();
    }
    
    public void addHolding(String symbol, int quantity, double price) {
        int currentQty = holdings.getOrDefault(symbol, 0);
        holdings.put(symbol, currentQty + quantity);
        
        double amount = quantity * price;
        balance -= amount;
        
        transactionHistory.add(new Transaction("BUY", symbol, quantity, price));
    }
    
    public void sellHolding(String symbol, int quantity, double price) {
        if (holdings.containsKey(symbol) && holdings.get(symbol) >= quantity) {
            holdings.put(symbol, holdings.get(symbol) - quantity);
            
            double amount = quantity * price;
            balance += amount;
            
            transactionHistory.add(new Transaction("SELL", symbol, quantity, price));
        }
    }
    
    public int getHolding(String symbol) {
        return holdings.getOrDefault(symbol, 0);
    }
    
    public Map<String, Integer> getHoldings() {
        return new HashMap<>(holdings);
    }
    
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    
    public double getPortfolioValue(Map<String, Stock> stocks) {
        double value = 0;
        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
            if (stocks.containsKey(entry.getKey())) {
                Stock stock = stocks.get(entry.getKey());
                value += stock.getCurrentPrice() * entry.getValue();
            }
        }
        return value;
    }
    
    public double getTotalAssets(Map<String, Stock> stocks) {
        return balance + getPortfolioValue(stocks);
    }
    
    public double getProfitLoss(Map<String, Stock> stocks) {
        return getTotalAssets(stocks) - initialBalance;
    }
    
    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }
    
    public List<Transaction> getRecentTransactions(int count) {
        int start = Math.max(0, transactionHistory.size() - count);
        return new ArrayList<>(transactionHistory.subList(start, transactionHistory.size()));
    }
    
    public static class Transaction implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String type; // BUY or SELL
        private String symbol;
        private int quantity;
        private double price;
        private long timestamp;
        
        public Transaction(String type, String symbol, int quantity, double price) {
            this.type = type;
            this.symbol = symbol;
            this.quantity = quantity;
            this.price = price;
            this.timestamp = System.currentTimeMillis();
        }
        
        public String getType() { return type; }
        public String getSymbol() { return symbol; }
        public int getQuantity() { return quantity; }
        public double getPrice() { return price; }
        public double getTotal() { return quantity * price; }
        public long getTimestamp() { return timestamp; }
        
        public String getDateString() {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
            return sdf.format(new java.util.Date(timestamp));
        }
    }
}
