import java.io.Serializable;

public class Stock implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String symbol;
    private String companyName;
    private double currentPrice;
    private double previousPrice;
    private double priceChange;
    private double percentageChange;
    
    public Stock(String symbol, String companyName, double price) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.currentPrice = price;
        this.previousPrice = price;
        this.priceChange = 0;
        this.percentageChange = 0;
    }
    
    public String getSymbol() { return symbol; }
    public String getCompanyName() { return companyName; }
    public double getCurrentPrice() { return currentPrice; }
    public double getPreviousPrice() { return previousPrice; }
    public double getPriceChange() { return priceChange; }
    public double getPercentageChange() { return percentageChange; }
    
    public void updatePrice(double newPrice) {
        this.previousPrice = this.currentPrice;
        this.currentPrice = newPrice;
        this.priceChange = newPrice - previousPrice;
        this.percentageChange = (priceChange / previousPrice) * 100;
    }
    
    @Override
    public String toString() {
        return companyName + " (" + symbol + ") - ₹" + currentPrice;
    }
}
