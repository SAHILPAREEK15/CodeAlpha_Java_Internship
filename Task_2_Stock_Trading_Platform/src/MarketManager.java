import java.io.*;
import java.util.*;

public class MarketManager {
    private static final String DATA_DIR = "data";
    private static final String MARKET_FILE = DATA_DIR + "/market.txt";
    
    private Map<String, Stock> stocks;
    private Portfolio portfolio;
    private Random random;
    
    public MarketManager() {
        this.stocks = new HashMap<>();
        this.random = new Random();
        createDataDirectoryIfNeeded();
        initializeMarket();
        loadPortfolio();
    }
    
    private void createDataDirectoryIfNeeded() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    private void initializeMarket() {
        // Create default stocks
        String[][] stockData = {
            {"TCS", "Tata Consultancy Services", "3850"},
            {"INFY", "Infosys", "1650"},
            {"RELIANCE", "Reliance Industries", "2450"},
            {"HDFC", "HDFC Bank", "1680"},
            {"ICICIBANK", "ICICI Bank", "920"},
            {"WIPRO", "Wipro", "410"},
            {"HCLTECH", "HCL Technologies", "1540"}
        };
        
        for (String[] data : stockData) {
            stocks.put(data[0], new Stock(data[0], data[1], Double.parseDouble(data[2])));
        }
        
        loadMarketData();
    }
    
    public void updateMarketPrices() {
        for (Stock stock : stocks.values()) {
            double change = (random.nextDouble() - 0.5) * 200; // -100 to +100 variation
            double newPrice = Math.max(stock.getCurrentPrice() + change, 100);
            stock.updatePrice(newPrice);
        }
        saveMarketData();
    }
    
    public Stock getStock(String symbol) {
        return stocks.get(symbol);
    }
    
    public Map<String, Stock> getAllStocks() {
        return new HashMap<>(stocks);
    }
    
    public List<Stock> searchStocks(String query) {
        List<Stock> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        for (Stock stock : stocks.values()) {
            if (stock.getSymbol().toLowerCase().contains(lowerQuery) ||
                stock.getCompanyName().toLowerCase().contains(lowerQuery)) {
                results.add(stock);
            }
        }
        return results;
    }
    
    public List<Stock> getTopGainers(int count) {
        return stocks.values().stream()
                .sorted((s1, s2) -> Double.compare(s2.getPercentageChange(), s1.getPercentageChange()))
                .limit(count)
                .toList();
    }
    
    public List<Stock> getTopLosers(int count) {
        return stocks.values().stream()
                .sorted((s1, s2) -> Double.compare(s1.getPercentageChange(), s2.getPercentageChange()))
                .limit(count)
                .toList();
    }
    
    // Portfolio management
    public Portfolio getPortfolio() {
        return portfolio;
    }
    
    public boolean buyStock(String symbol, int quantity) {
        Stock stock = stocks.get(symbol);
        if (stock == null) return false;
        
        double totalCost = stock.getCurrentPrice() * quantity;
        if (portfolio.getBalance() < totalCost) return false;
        
        portfolio.addHolding(symbol, quantity, stock.getCurrentPrice());
        savePortfolio();
        return true;
    }
    
    public boolean sellStock(String symbol, int quantity) {
        if (portfolio.getHolding(symbol) < quantity) return false;
        
        Stock stock = stocks.get(symbol);
        if (stock == null) return false;
        
        portfolio.sellHolding(symbol, quantity, stock.getCurrentPrice());
        savePortfolio();
        return true;
    }
    
    public void addBalance(double amount) {
        portfolio.setBalance(portfolio.getBalance() + amount);
        savePortfolio();
    }
    
    private void saveMarketData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(MARKET_FILE))) {
            oos.writeObject(stocks);
        } catch (IOException e) {
            System.err.println("Error saving market data: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadMarketData() {
        File file = new File(MARKET_FILE);
        if (!file.exists()) return;
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            stocks = (Map<String, Stock>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading market data: " + e.getMessage());
        }
    }
    
    private void savePortfolio() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_DIR + "/portfolio.txt"))) {
            oos.writeObject(portfolio);
        } catch (IOException e) {
            System.err.println("Error saving portfolio: " + e.getMessage());
        }
    }
    
    private void loadPortfolio() {
        File file = new File(DATA_DIR + "/portfolio.txt");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                portfolio = (Portfolio) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading portfolio: " + e.getMessage());
                portfolio = new Portfolio(50000);
            }
        } else {
            portfolio = new Portfolio(50000);
        }
    }
}
