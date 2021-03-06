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
                String uri = httpExchange.getRequestURI().toString();
                if (reqHeaders.containsKey("Authorization")) {
                    String authToken = reqHeaders.getFirst("Authorization");
                    String response;
                    //Reads the url to see if there is a person ID inside it
                    String personID = getPersonID(uri);
                    if(!personID.equals("")) {
                        response = Deserialize.serialize(pService.getPerson(authToken, personID));
                    } else {
                        response = Deserialize.serialize(pService.getFamily(authToken));
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

    private String getPersonID(String uri) {
        String personID = "";
        if(uri.length() > 9) {
            personID = uri.substring(8, uri.length());
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
