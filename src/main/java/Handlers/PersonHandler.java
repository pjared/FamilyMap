package Handlers;

import Service.PersonService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.HttpURLConnection;

public class PersonHandler extends FileHandler {
    private PersonService pService = new PersonService();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            if (httpExchange.getRequestMethod().toUpperCase().equals("GET")) {
                Headers reqHeaders = httpExchange.getRequestHeaders();
                InputStream reqBody = httpExchange.getRequestBody();
                String reqData = readString(reqBody);
                System.out.println(reqData);

                //might just do a checkToken class
                if (reqHeaders.containsKey("Authorization")) {
                    String authToken = reqHeaders.getFirst("Authorization");
                    if (authToken.equals("afj232hj2332")) {
                    }else {
                        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                    }
                } else {
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                }

                String response;
                if(1 == 1) { //TODO: Add a reader to check if there is a given personID
                    response = Deserialize.serialize(pService.getPerson("123", "123"));
                } else {
                    response = Deserialize.serialize(pService.getFamily("123"));
                }

                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream respBody = httpExchange.getResponseBody();
                writeString(response, respBody);
                respBody.close();
            } else {
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
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
