const path = require('path');
const grpc = require('@grpc/grpc-js');
const protoLoader = require('@grpc/proto-loader');
const mysql = require('mysql2/promise');

// Path to .proto file
const PROTO_PATH = path.join(__dirname, 'order.proto');

// Load the protobuf
const packageDefinition = protoLoader.loadSync(PROTO_PATH, {
  keepCase: true,
  longs: String,
  enums: String,
  defaults: true,
  oneofs: true
});
const orderProto = grpc.loadPackageDefinition(packageDefinition).order;

require('dotenv').config();
// Create a MySQL connection pool
const pool = mysql.createPool({
  host: process.env.DATABASE_HOST,
  user: process.env.DATABASE_USER,
  password: process.env.DATABASE_PASSWORD,
  database: process.env.DATABASE_NAME
});

// Implementation of the calculateTotal gRPC
async function calculateTotal(call, callback) {
  const { productId, quantity } = call.request;

  try {
    const price = await getPriceFromDatabase(productId);
    const totalPrice = price * quantity;

    callback(null, { totalPrice });
  } catch (error) {
    console.error('Error in calculateTotal:', error);
    callback({
      code: grpc.status.INTERNAL,
      message: error.message
    });
  }
}

// Helper function to query DB
async function getPriceFromDatabase(productId) {
  const [rows] = await pool.query('SELECT price FROM products WHERE id = ?', [productId]);
  if (rows.length === 0) {
    throw new Error(`Product not found: ${productId}`);
  }
  return rows[0].price;
}

// Start the gRPC server
function main() {
  const server = new grpc.Server();
  server.addService(orderProto.OrderService.service, {
    calculateTotal
  });

  const address = '0.0.0.0:50051';
  server.bindAsync(address, grpc.ServerCredentials.createInsecure(), (err, port) => {
    if (err) {
      return console.error(err);
    }
    console.log(`gRPC server running at http://${address}`);
    // server.start();
  });
}

main();