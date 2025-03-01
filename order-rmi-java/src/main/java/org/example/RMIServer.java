package org.example;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) {
        try {
            // Make RMI embed public IP in the stub:
            System.setProperty("java.rmi.server.hostname", "13.212.19.81");

            // Implementation
            OrderService orderService = new OrderServiceImpl();

            // Create the registry on port 1099
            Registry registry = LocateRegistry.createRegistry(1099);

            // Bind the service
            registry.rebind("OrderService", orderService);

            System.out.println("RMI Server is running, registry at 1099, remote object at 1100");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
