package Handlers;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

    public static void main(String[] args) {
        String portNumber = args[0];

        try {
            new Server().startServer(Integer.parseInt(portNumber));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //localhost:8080
    }

    private void startServer(int port) throws IOException {
        InetSocketAddress serverAddress = new InetSocketAddress(port);
        HttpServer server = HttpServer.create(serverAddress, 10);
        registerHandlers(server);
        server.start();
        System.out.println("FamilyMapServer listening on port " + port);
    }

    private void registerHandlers(HttpServer server) {
        server.createContext("/index.html", new FileHandler());
        server.createContext("/", new FileHandler());
        server.createContext("/user/register", new RegisterRequestHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/user/login", new LoginHandler());
    }
}