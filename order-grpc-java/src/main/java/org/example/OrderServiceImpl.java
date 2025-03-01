package org.example;

import io.grpc.stub.StreamObserver;
import order.Order.OrderRequest;
import order.Order.OrderResponse;
import order.OrderServiceGrpc;

import java.rmi.RemoteException;
import java.sql.*;

public class OrderServiceImpl extends OrderServiceGrpc.OrderServiceImplBase {

    @Override
    public void calculateTotal(OrderRequest request, StreamObserver<OrderResponse> responseObserver) {
        String productId = request.getProductId();
        int quantity = request.getQuantity();

        double price = 0;
        try {
            price = getPrice(productId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        double total = price * quantity;

        OrderResponse response = OrderResponse.newBuilder()
                .setTotalPrice(total)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private double getPrice(String productId) throws RemoteException {
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
