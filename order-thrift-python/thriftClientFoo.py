# FOR TESTING PURPOSE ONLY

from thrift import Thrift
from thrift.transport import TSocket, TTransport
from thrift.protocol import TBinaryProtocol

# Import the generated code from gen-py
from gen_py.order_service import OrderService

def main():
    # Connect to server
    transport = TSocket.TSocket('localhost', 9090)
    transport = TTransport.TBufferedTransport(transport)
    protocol = TBinaryProtocol.TBinaryProtocol(transport)

    # Create a client to use the OrderService
    client = OrderService.Client(protocol)

    transport.open()
    print("Connected to Thrift server...")

    # Make a remote call
    total = client.calculateTotal("p0", 1)
    print(f"Total: {total}")

    transport.close()

if __name__ == "__main__":
    main()