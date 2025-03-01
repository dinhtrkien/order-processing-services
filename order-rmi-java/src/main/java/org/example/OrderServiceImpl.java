package org.example;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderServiceImpl extends UnicastRemoteObject implements OrderService{
    protected OrderServiceImpl() throws RemoteException {
        super(1100); // Fixed port for remote object call
    }

    @Override
    public double calculateTotal(String productId, int quantity) throws RemoteException {
        double price = getPriceFromDatabase(productId);
        return price * quantity;
    }

    public double getPriceFromDatabase(String productId) throws RemoteException {
        double price;

        String url = EnvLoader.getDatabaseUrl();
        String user = EnvLoader.getDatabaseUser();
        String pass = EnvLoader.getDatabasePassword();

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement("SELECT price FROM products WHERE id = ?")) {
            stmt.setString(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    price = rs.getDouble("price");
                } else {
                    throw new SQLException("No product found for ID = " + productId);
                }
            }
        } catch (SQLException e) {
            throw new RemoteException("Database error", e);
        }

        return price;
    }
}
