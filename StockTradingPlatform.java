// StockTradingPlatform.java
// Console-based Stock Trading Platform using JDBC and MySQL

import java.sql.*;
import java.util.*;

public class StockTradingPlatform {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/stockdb";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";
    private static Connection conn;

    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. View Stocks\n2. Buy Stock\n3. Sell Stock\n4. View Portfolio\n5. View Transactions\n6. Exit");
            System.out.print("Choose: ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1 -> viewStocks();
                case 2 -> buyStock(sc);
                case 3 -> sellStock(sc);
                case 4 -> viewPortfolio();
                case 5 -> viewTransactions();
                case 6 -> {
                    conn.close();
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private static void viewStocks() throws SQLException {
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM stocks");
        System.out.println("\n--- Market Data ---");
        while (rs.next()) {
            System.out.printf("%s - %s: $%.2f\n", rs.getString("symbol"), rs.getString("name"), rs.getDouble("price"));
        }
    }

    private static void buyStock(Scanner sc) throws SQLException {
        System.out.print("Enter stock symbol: ");
        String symbol = sc.next().toUpperCase();
        System.out.print("Enter quantity: ");
        int qty = sc.nextInt();

        PreparedStatement getStock = conn.prepareStatement("SELECT price FROM stocks WHERE symbol = ?");
        getStock.setString(1, symbol);
        ResultSet rs = getStock.executeQuery();
        if (!rs.next()) {
            System.out.println("Invalid symbol");
            return;
        }
        double price = rs.getDouble("price");
        double cost = qty * price;

        PreparedStatement updateHold = conn.prepareStatement(
            "INSERT INTO holdings (symbol, quantity, avg_price) VALUES (?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE quantity = quantity + ?, avg_price = (avg_price + VALUES(avg_price)) / 2");
        updateHold.setString(1, symbol);
        updateHold.setInt(2, qty);
        updateHold.setDouble(3, price);
        updateHold.setInt(4, qty);
        updateHold.executeUpdate();

        PreparedStatement insertTxn = conn.prepareStatement("INSERT INTO transactions (symbol, quantity, price, type) VALUES (?, ?, ?, 'BUY')");
        insertTxn.setString(1, symbol);
        insertTxn.setInt(2, qty);
        insertTxn.setDouble(3, price);
        insertTxn.executeUpdate();

        System.out.printf("Bought %d shares of %s at $%.2f each.\n", qty, symbol, price);
    }

    private static void sellStock(Scanner sc) throws SQLException {
        System.out.print("Enter stock symbol: ");
        String symbol = sc.next().toUpperCase();
        System.out.print("Enter quantity: ");
        int qty = sc.nextInt();

        PreparedStatement getHold = conn.prepareStatement("SELECT quantity FROM holdings WHERE symbol = ?");
        getHold.setString(1, symbol);
        ResultSet rs = getHold.executeQuery();
        if (!rs.next() || rs.getInt("quantity") < qty) {
            System.out.println("Insufficient quantity");
            return;
        }

        PreparedStatement getStock = conn.prepareStatement("SELECT price FROM stocks WHERE symbol = ?");
        getStock.setString(1, symbol);
        ResultSet priceRs = getStock.executeQuery();
        priceRs.next();
        double price = priceRs.getDouble("price");

        PreparedStatement updateHold = conn.prepareStatement("UPDATE holdings SET quantity = quantity - ? WHERE symbol = ?");
        updateHold.setInt(1, qty);
        updateHold.setString(2, symbol);
        updateHold.executeUpdate();

        PreparedStatement insertTxn = conn.prepareStatement("INSERT INTO transactions (symbol, quantity, price, type) VALUES (?, ?, ?, 'SELL')");
        insertTxn.setString(1, symbol);
        insertTxn.setInt(2, qty);
        insertTxn.setDouble(3, price);
        insertTxn.executeUpdate();

        System.out.printf("Sold %d shares of %s at $%.2f each.\n", qty, symbol, price);
    }

    private static void viewPortfolio() throws SQLException {
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM holdings");
        System.out.println("\n--- Portfolio ---");
        while (rs.next()) {
            System.out.printf("%s - Qty: %d, Avg Price: $%.2f\n", rs.getString("symbol"), rs.getInt("quantity"), rs.getDouble("avg_price"));
        }
    }

    private static void viewTransactions() throws SQLException {
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM transactions");
        System.out.println("\n--- Transactions ---");
        while (rs.next()) {
            System.out.printf("%s %d shares of %s at $%.2f\n", rs.getString("type"), rs.getInt("quantity"), rs.getString("symbol"), rs.getDouble("price"));
        }
    }
}

/*
SQL setup for MySQL:

CREATE DATABASE stockdb;
USE stockdb;

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

-- Insert dummy stock data
INSERT INTO stocks VALUES ('AAPL', 'Apple Inc', 175.5);
INSERT INTO stocks VALUES ('GOOG', 'Alphabet Inc', 134.2);
INSERT INTO stocks VALUES ('TSLA', 'Tesla Motors', 192.0);
*/
