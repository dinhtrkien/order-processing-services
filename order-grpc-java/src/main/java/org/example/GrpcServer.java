package org.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class GrpcServer {
    public static void main(String[] args) {
        int port = 50051;
        try {
            Server server = ServerBuilder.forPort(port)
                    .addService(new OrderServiceImpl())
                    .build()
                    .start();

            System.out.println("gRPC server started. Listening on port " + port);

            server.awaitTermination();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
