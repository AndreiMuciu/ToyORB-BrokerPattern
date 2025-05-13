const { ClientProxy } = require("./ClientProxy");

const proxy = new ClientProxy();

function askAgain() {
  proxy.rl.question(
    "On which server do you want to send the command? (MathServer / InfoServer) or 'exit' to exit: ",
    (serverName) => {
      if (
        serverName.toLowerCase() === "exit" ||
        serverName.toLowerCase() === "stop"
      ) {
        console.log("Closing the client...");
        proxy.rl.close();
        process.exit();
      }

      proxy.rl.question(
        "Write the command (e.g., getTemp Bucharest): ",
        (command) => {
          proxy.callServer(serverName, command, askAgain);
        }
      );
    }
  );
}

askAgain();
