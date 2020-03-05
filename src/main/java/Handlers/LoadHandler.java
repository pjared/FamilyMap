package Handlers;

import DAOs.DataAccessException;
import Requests.LoadRequest;
import Service.LoadService;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.HttpURLConnection;

public class LoadHandler extends FileHandler{
    private Deserialize decereal = new Deserialize();
    private LoadService lService = new LoadService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);
                System.out.println(reqData);
                LoadRequest lObject = decereal.deserialize(reqData, LoadRequest.class);
                String response = decereal.serialize(lService.load(lObject));

                OutputStream respBody = exchange.getResponseBody();
                writeString(response, respBody);
                respBody.close();
                System.out.println("LoadResponse: " + response);
            }
            exchange.getResponseBody().close();
        } catch (IOException | DataAccessException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            exchange.getResponseBody().close();
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
