const net = require("net");
const readline = require("readline");

class ClientProxy {
  constructor() {
    this.rl = readline.createInterface({
      input: process.stdin,
      output: process.stdout,
    });
  }

  lookupServer(serverName, callback) {
    const client = new net.Socket();
    client.connect(5000, "127.0.0.1", () => {
      client.write(serverName + "\n");
    });

    client.on("data", (data) => {
      const address = data.toString().trim();
      if (address === "NOT_FOUND") {
        //console.log("Server not found!");
        callback(null);
      } else {
        const [ip, port] = address.split(":");
        callback({ ip, port: parseInt(port) });
      }
      client.destroy();
    });
  }

  sendRequest(ip, port, request, callbackAfter) {
    const client = new net.Socket();
    client.connect(port, ip, () => {
      console.log(`Connected to server at ${ip}:${port}`);
      client.write(request + "\n");
    });

    client.on("data", (data) => {
      console.log("Server response:", data.toString());
      client.destroy();
    });

    client.on("close", () => {
      console.log("Connection closed\n");
      callbackAfter();
    });
  }

  callServer(serverName, request, callbackAfter) {
    this.lookupServer(serverName, (server) => {
      if (server) {
        this.sendRequest(server.ip, server.port, request, callbackAfter);
      } else {
        console.log("Server not found! Try Again.\n");
        callbackAfter();
      }
    });
  }
}

module.exports = { ClientProxy };
