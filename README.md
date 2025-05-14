# CodeAlpha_Stock_Trading_Platform
# 💹 Stock Trading Platform (Console-Based)

A simple console-based Stock Trading Platform built with **Java**, **JDBC**, and **MySQL**.  
This platform simulates real-world trading actions like viewing market stocks, buying/selling shares, and managing a portfolio.

## 📋 Features

- 📈 View market data (stocks with price)
- 💰 Buy and sell stocks
- 📊 View your current portfolio
- 📄 Track all your transactions

## 🛠 Technologies Used

- Java (JDK 17 or above recommended)
- JDBC
- MySQL (with MySQL Workbench or CLI)
- MySQL Connector/J (JDBC driver)

---

## 🧱 Database Setup

1. **Create a database named `stockdb`:**

CREATE DATABASE stockdb;
USE stockdb;

2. **Create Required Table:**
   
CREATE TABLE stocks (
    symbol VARCHAR(10) PRIMARY KEY,
    name VARCHAR(50),
    price DOUBLE
);

CREATE TABLE holdings (
    symbol VARCHAR(10) PRIMARY KEY,
    quantity INT,
    avg_price DOUBLE
);

CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    symbol VARCHAR(10),
    quantity INT,
    price DOUBLE,
    type VARCHAR(10),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

3. **Insert Dummy Data:**
         INSERT INTO stocks VALUES ('AAPL', 'Apple Inc', 175.5);
         INSERT INTO stocks VALUES ('GOOG', 'Alphabet Inc', 134.2);
         INSERT INTO stocks VALUES ('TSLA', 'Tesla Motors', 192.0);

⚙️ How to Run
1.Download MySQL Connector/J from here.

2.Place the .jar file (like mysql-connector-java-8.0.33.jar) in the project folder.

3.Compile the code: javac -cp ".;mysql-connector-java-8.0.33.jar" StockTradingPlatform.java

4.Run the code: java -cp ".;mysql-connector-java-8.0.33.jar" StockTradingPlatform

🖥️ Sample Console Output:

1. View Stocks
2. Buy Stock
3. Sell Stock
4. View Portfolio
5. View Transactions
6. Exit
Choose: 1

--- Market Data ---
AAPL - Apple Inc: $175.50
GOOG - Alphabet Inc: $134.20
TSLA - Tesla Motors: $192.00

1. View Stocks
2. Buy Stock
3. Sell Stock
4. View Portfolio
5. View Transactions
6. Exit
Choose: 2
Enter stock symbol: AAPL
Enter quantity: 10
Bought 10 shares of AAPL at $175.50 each.

1. View Stocks
2. Buy Stock
3. Sell Stock
4. View Portfolio
5. View Transactions
6. Exit
Choose: 4

--- Portfolio ---
AAPL - Qty: 10, Avg Price: $175.50

1. View Stocks
2. Buy Stock
3. Sell Stock
4. View Portfolio
5. View Transactions
6. Exit
Choose: 3
Enter stock symbol: AAPL
Enter quantity: 5
Sold 5 shares of AAPL at $175.50 each.

1. View Stocks
2. Buy Stock
3. Sell Stock
4. View Portfolio
5. View Transactions
6. Exit
Choose: 5

--- Transactions ---
BUY 10 shares of AAPL at $175.50
SELL 5 shares of AAPL at $175.50

1. View Stocks
2. Buy Stock
3. Sell Stock
4. View Portfolio
5. View Transactions
6. Exit
Choose: 6


