# Task 2: Stock Trading Platform - Simulated

## Project Overview
A simulated stock trading platform built in Java Swing that allows users to buy/sell stocks, track portfolio performance, and manage transactions in a risk-free environment.

**IMPORTANT**: This is a SIMULATION. No real money is involved. All trading happens in a virtual environment with play money.

## Features
- **Dashboard**: Portfolio summary, daily P&L, available balance
- **Market Screen**: Live (simulated) stock prices with automatic updates
- **Stock List**: 7 Indian company stocks with real-time price simulation
- **Buy/Sell Operations**: 
  - Select stock and quantity
  - Automatic balance verification
  - Transaction recording
- **Portfolio Management**: View current holdings and their values
- **Watchlist**: Track favorite stocks
- **Transaction History**: Complete buy/sell history with timestamps
- **Simulated Market**: Prices update automatically every 5 seconds

## Technologies Used
- Java 17+
- Java Swing for GUI
- Timer for market simulation
- Collections Framework
- Object serialization for persistence

## OOP Concepts Used
- **Encapsulation**: Stock, Portfolio, Booking classes
- **Inheritance**: Panel classes extending JPanel
- **Polymorphism**: Button renderers and editors
- **Abstraction**: MarketManager hiding market logic
- **Collections**: HashMap for stock storage, ArrayList for transactions

## Stock List
- TCS (Tata Consultancy Services)
- INFY (Infosys Limited)
- RELIANCE (Reliance Industries)
- HDFC (HDFC Bank)
- ICICIBANK (ICICI Bank)
- WIPRO (Wipro Limited)
- HCLTECH (HCL Technologies)

## Project Structure
```
Task_2_Stock_Trading_Platform/
├── src/
│   ├── StockTradingApp.java             (Main application)
│   ├── Stock.java                       (Stock model)
│   ├── Portfolio.java                   (User portfolio)
│   ├── MarketManager.java               (Market & trading logic)
│   ├── DashboardPanel2.java             (Dashboard)
│   ├── MarketPanel2.java                (Market view)
│   ├── PortfolioPanel2.java             (Holdings view)
│   ├── WatchlistPanel2.java             (Watchlist)
│   ├── TransactionPanel2.java           (Transaction history)
│   ├── BuyDialog.java                   (Buy dialog)
│   └── SellDialog.java                  (Sell dialog)
├── data/
│   ├── market.txt                       (Stock data)
│   └── portfolio.txt                    (Portfolio data)
└── README.md
```

## Compilation & Execution

### Compile:
```bash
cd Task_2_Stock_Trading_Platform/src
javac *.java
```

### Run:
```bash
java StockTradingApp
```

## How to Use

1. **Dashboard**: Overview of portfolio value, daily P&L, balance
2. **Market**: 
   - View all stocks with current prices
   - Click "Buy" to purchase stocks
3. **Portfolio**: 
   - View your holdings
   - Click "Sell" to sell stocks
4. **Watchlist**: Track your favorite stocks
5. **Transactions**: View complete trading history

## Trading Rules
- Starting balance: ₹50,000
- Cannot buy without sufficient balance
- Cannot sell stocks you don't own
- Prices update automatically every 5 seconds
- All transactions are recorded

## Input Validation
- Quantity must be positive
- Cannot buy/sell zero shares
- Balance must be sufficient for purchase
- Can only sell owned quantities

## Data Storage
- Market data: `data/market.txt`
- Portfolio data: `data/portfolio.txt`
- Data persists between sessions

## DISCLAIMER
This is a simulated trading platform for LEARNING purposes only. It does NOT connect to real stock exchanges or process real money. The stock prices and trades are entirely simulated.

## Limitations
- Prices are simulated, not real market data
- No real trade execution
- Single user system
- Limited to 7 stocks

## Future Improvements
- Integration with real stock market APIs
- Real-time market data from exchanges
- Advanced charting with candlesticks
- Technical indicators (RSI, MACD, Bollinger Bands)
- Multi-user support with authentication
- Portfolio analysis and recommendations
- Practice mode with virtual money for learning
