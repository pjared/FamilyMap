package Handlers;

import Requests.FillRequest;
import Service.FillService;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.HttpURLConnection;

public class FillHandler extends FileHandler{
    private Deserialize decereal = new Deserialize();
    private FillService fService = new FillService();
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            if (httpExchange.getRequestMethod().toUpperCase().equals("POST")) {
                InputStream reqBody = httpExchange.getRequestBody();
                String reqData = readString(reqBody);
                System.out.println(reqData);

                //Reads the url to get the username and generations
                String uri = httpExchange.getRequestURI().toString();
                String userName = getUsername(uri);
                int generations = getGenerations(uri);
                FillRequest fObject = new FillRequest(userName, generations);
                String response = decereal.serialize(fService.fill(fObject));

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
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        } catch (IOException e) {
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            httpExchange.getResponseBody().close();
            e.printStackTrace();
        }
    }

    private String getUsername(String uri) {
        String userName = "";
        if(uri.length() > 5) {
            userName = uri.substring(6, uri.length());
            userName = userName.substring(0, userName.indexOf('/'));
        }
        return userName;
    }

    private int getGenerations(String uri) {
        String generations = "";
        if(uri.length() > 9) {
            generations = uri.substring(6, uri.length());
            generations = generations.substring(generations.indexOf('/') + 1, generations.length());
        }
        if(generations == null) {
            return 4;
        }
        return Integer.parseInt(generations);
    }


    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(sw);
        bw.write(str);
        bw.flush();
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
}
