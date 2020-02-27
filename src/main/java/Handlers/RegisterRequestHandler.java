package Handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class RegisterRequestHandler extends FileHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        File file = new File("web/HTML/404.html");
        httpExchange.sendResponseHeaders(200, file.length());
        try (OutputStream os = httpExchange.getResponseBody()) {
            Files.copy(file.toPath(), os);
        }
    }
}
