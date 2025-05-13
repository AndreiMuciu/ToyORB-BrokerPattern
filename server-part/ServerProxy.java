import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;

public class ServerProxy {
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (args.length != 2) {
            System.out.println("Usage: java ServerProxy <localPort> <serverType>");
            return;
        }

        int localPort = Integer.parseInt(args[0]);
        String serverType = args[1];

        ServerSocket serverSocket = new ServerSocket(localPort);
        System.out.println("ServerProxy listening on port " + localPort + " for " + serverType);

        try (Socket socket = new Socket("localhost", 5000);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String message = "Register: " + serverType + " " + localPort;

            out.println(message);

            System.out.println("Sent message to dispatcher: " + message);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            System.out.println("Received response from dispatcher: " + response);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }


        Class<?> serverClass = Class.forName(serverType);
        Object serverInstance = serverClass.getDeclaredConstructor().newInstance();

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                BufferedReader clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter clientOut = new PrintWriter(clientSocket.getOutputStream(), true);

                String request = clientIn.readLine();
                System.out.println(serverType + "'s proxy received request: " + request);

                String methodName = request.split(" ")[0];
                String[] params = request.split(" ")[1].split(",");

                Method targetMethod = null;
                for (Method method : serverClass.getDeclaredMethods()) {
                    if (method.getName().equals(methodName) && method.getParameterCount() == params.length) {
                        targetMethod = method;
                        break;
                    }
                }

                if (targetMethod == null) {
                    clientOut.println("Method not found in " + serverType + ".");
                    clientSocket.close();
                    continue;
                }

                Class<?>[] paramTypes = targetMethod.getParameterTypes();
                Object[] convertedParams = new Object[params.length];

                for (int i = 0; i < params.length; i++) {
                    String param = params[i].trim();
                    Class<?> type = paramTypes[i];

                    if (type == int.class) {
                        convertedParams[i] = Integer.parseInt(param);
                    } else if (type == double.class) {
                        convertedParams[i] = Double.parseDouble(param);
                    } else if (type == boolean.class) {
                        convertedParams[i] = Boolean.parseBoolean(param);
                    } else {
                        convertedParams[i] = param; // Assuming it's a String
                    }
                }

                Object result = targetMethod.invoke(serverInstance, convertedParams);
                clientOut.println("Result: " + result);

                clientSocket.close();
            }catch (Exception e) {
                System.out.println("Error processing request: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
