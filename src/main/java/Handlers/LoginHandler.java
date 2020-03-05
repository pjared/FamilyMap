package Handlers;

import Requests.LoginRequest;
import Service.LoginService;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.HttpURLConnection;

public class LoginHandler extends FileHandler {
    private Deserialize decereal = new Deserialize();
    private LoginService lService = new LoginService();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            if (httpExchange.getRequestMethod().toUpperCase().equals("POST")) {
                InputStream reqBody = httpExchange.getRequestBody();
                String reqData = readString(reqBody);
                System.out.println(reqData);

                LoginRequest lObject = decereal.deserialize(reqData, LoginRequest.class);
                String response = decereal.serialize(lService.login(lObject));

                if(response.contains("Request property missing or has invalid value")) {
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }

                if(response.contains("true")) {
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }
                OutputStream respBody = httpExchange.getResponseBody();
                writeString(response, respBody);
                respBody.close();
            } else {
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
        sw.write(str);
        sw.flush();
    }
}
