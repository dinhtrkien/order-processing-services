package org.example;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {
//    public static String host = EnvLoader.getHost();
    // Static method to retrieve the remote OrderService stub, this will be used in the Node.js bridge
    public static OrderService getOrderService() {
        try {
            Registry registry = LocateRegistry.getRegistry("13.212.19.81", 1099);
            OrderService stub = (OrderService) registry.lookup("OrderService");
            return stub;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            // for testing purpose only
            Registry registry = LocateRegistry.getRegistry("13.212.19.81", 1099);
            OrderService stub = (OrderService) registry.lookup("OrderService");
            int quantity = 1;
            String productId = "p1999";
            System.out.println("Order confirmed! Total price: " + stub.calculateTotal(productId, quantity));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}