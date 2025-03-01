from thrift import Thrift
from thrift.transport import TSocket, TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer
import os
from dotenv import load_dotenv

# Load environment variables from .env file
load_dotenv()

# Import the generated code from gen-py
from gen_py.order_service import OrderService

import mysql.connector

class OrderServiceHandler:
    def __init__(self):
        self.conn = mysql.connector.connect(
            host=os.getenv("DATABASE_HOST"),
            user=os.getenv("DATABASE_USER"),
            password=os.getenv("DATABASE_PASSWORD"),
            database=os.getenv("DATABASE_NAME")
        )

    def calculateTotal(self, productId, quantity):
            # Query the product price from the DB
            cursor = self.conn.cursor()
            cursor.execute("SELECT price FROM products WHERE id = %s", (productId,))
            row = cursor.fetchone()
            if not row:
                raise Exception(f"Product {productId} not found.")
            price = row[0]
            return price * quantity

# Set up the Thrift processor, transport, and protocol for the server
if __name__ == "__main__":
    handler = OrderServiceHandler()
    processor = OrderService.Processor(handler)

    transport = TSocket.TServerSocket(host='0.0.0.0', port=9090)
    tfactory = TTransport.TBufferedTransportFactory()
    pfactory = TBinaryProtocol.TBinaryProtocolFactory()

    server = TServer.TSimpleServer(processor, transport, tfactory, pfactory)
    print("Starting Thrift server on port 9090...")
    server.serve()
    print("Server stopped.")