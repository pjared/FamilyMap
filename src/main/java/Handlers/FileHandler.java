package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

class FileHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String urlPath = httpExchange.getRequestURI().toString();
        String filePath = "web" + urlPath;
        File file = new File(filePath);
        if(file.exists()) {
            if (urlPath == null || urlPath.equals("/") || urlPath.equals("/index.html")) {
                file = new File("web/index.html");
                httpExchange.sendResponseHeaders(200, file.length());
                try (OutputStream os = httpExchange.getResponseBody()) {
                    Files.copy(file.toPath(), os);
                }
            }
            httpExchange.sendResponseHeaders(200, file.length());
            try (OutputStream os = httpExchange.getResponseBody()) {
                Files.copy(file.toPath(), os);
            }
        } else {
            file = new File("web/HTML/404.html");
            httpExchange.sendResponseHeaders(404, file.length());
            try (OutputStream os = httpExchange.getResponseBody()) {
                Files.copy(file.toPath(), os);
            }
        }
    }
}
