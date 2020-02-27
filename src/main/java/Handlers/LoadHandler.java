package Handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

public class LoadHandler extends FileHandler{
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //TODO:Finsh writing this class
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            }
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
