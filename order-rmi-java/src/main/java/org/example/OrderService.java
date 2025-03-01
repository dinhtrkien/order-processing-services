package org.example;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface OrderService extends Remote {
    double calculateTotal(String productId, int quantity) throws RemoteException, SQLException;
}
