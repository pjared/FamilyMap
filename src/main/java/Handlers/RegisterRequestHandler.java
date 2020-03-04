package Handlers;

import Requests.RegisterRequest;
import Service.RegisterService;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.HttpURLConnection;

public class RegisterRequestHandler extends FileHandler {
    private Deserialize decereal = new Deserialize();
    private RegisterService rService = new RegisterService();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            if (httpExchange.getRequestMethod().toUpperCase().equals("POST")) {
                InputStream reqBody = httpExchange.getRequestBody();
                String reqData = readString(reqBody);
                System.out.println(reqData);
                RegisterRequest rObject = decereal.deserialize(reqData, RegisterRequest.class);
                String response = decereal.serialize(rService.register(rObject));

                if(response.contains("Username already taken by another user")) {
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                } else {
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }
                OutputStream respBody = httpExchange.getResponseBody();
                writeString(response, respBody);
                respBody.close();
            } else {
                // We expected a POST but got something else, so we return a "bad request"
                // status code to the client.
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }

            httpExchange.getResponseBody().close();
        } catch (IOException e) {
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            httpExchange.getResponseBody().close();

            e.printStackTrace();
        }
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
