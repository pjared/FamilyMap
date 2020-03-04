package Handlers;

import Service.EventService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.HttpURLConnection;

public class EventHandler extends FileHandler{
    private EventService eService = new EventService();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            if (httpExchange.getRequestMethod().toUpperCase().equals("GET")) {
                Headers reqHeaders = httpExchange.getRequestHeaders();
                InputStream reqBody = httpExchange.getRequestBody();
                String reqData = readString(reqBody);
                System.out.println(reqData);
                if (reqHeaders.containsKey("Authorization")) {
                    String authToken = reqHeaders.getFirst("Authorization");
                    String response;
                    String uri = httpExchange.getRequestURI().toString();
                    String eventID = getEventID(uri);

                    if(eventID != null) {
                        response = Deserialize.serialize(eService.getEvent(authToken, eventID));
                    } else {
                        response = Deserialize.serialize(eService.getAllEvents(authToken));
                    }

                    if(response.contains("error")) {
                        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    }
                    if(response.contains("true")) {
                        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    }

                    OutputStream respBody = httpExchange.getResponseBody();
                    writeString(response, respBody);
                    respBody.close();
                } else {
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                }
            } else {
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        } catch (IOException e) {
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            httpExchange.getResponseBody().close();

            e.printStackTrace();
        }
    }

    private String getEventID(String uri) {
        String personID = null;
        if(uri.length() > 9) {
            personID = uri.substring(7, uri.length());
        }
        return personID;
    }

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(sw);
        bw.write(str);
        bw.flush();
    }
}
