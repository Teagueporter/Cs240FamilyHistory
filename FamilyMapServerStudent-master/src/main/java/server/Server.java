package server;

import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;
import handlers.*;


public class Server {

    // The maximum number of waiting incoming connections to queue.
    private static final int MAX_WAITING_CONNECTIONS = 12;

    //initializing the http server
    private HttpServer server;

    //run function that takes in the port number for our website
    private void run(String portNumber) {

        System.out.println("Initializing HTTP Server");

        try {
            // Create a new HttpServer object.
            server = HttpServer.create(
                    new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Indicate that we are using the default "executor".
        server.setExecutor(null);

        //print out creating contexts for the log
        System.out.println("Creating contexts");

        //creates contexts for handlers
        server.createContext("/event", new EventHandler());
        server.createContext("/person", new PersonHandler());
        server.createContext("/fill", new FillHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/", new FileHandler());

        // Log message indicating that the HttpServer is about the start accepting
        // incoming client connections.
        System.out.println("Starting server");


        server.start();

        // Log message indicating that the server has successfully started.
        System.out.println("Server started");
    }

   //parameter is the port number
    public static void main(String[] args) {
        String portNumber = args[0];
        new Server().run(portNumber);
    }
}


